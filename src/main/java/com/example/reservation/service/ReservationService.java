package com.example.reservation.service;

import com.example.reservation.config.ReservationProperties;
import com.example.reservation.config.SeleniumProperties;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);
    private static final List<String> logMessages = Collections.synchronizedList(new ArrayList<>());

    private final ReservationProperties reservationProperties;
    private final SeleniumProperties seleniumProperties;

    public ReservationService(ReservationProperties reservationProperties,
                             SeleniumProperties seleniumProperties) {
        this.reservationProperties = reservationProperties;
        this.seleniumProperties = seleniumProperties;
    }

    public List<Map<String, String>> getAvailableSlots() {
        logMessages.clear();
        log("Starting search");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (seleniumProperties.isHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(seleniumProperties.getTimeout().getImplicit())
        );
        WebDriverWait wait = new WebDriverWait(driver,
            Duration.ofSeconds(seleniumProperties.getTimeout().getExplicit())
        );
        List<Map<String, String>> slots = new ArrayList<>();
        try {
            log("Starting login process");
            driver.get(reservationProperties.getLogin().getUrl());

            // Handle cookie banner if present
            try {
                WebElement cookieAccept = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'Accepteer alles')]")
                    )
                );
                cookieAccept.click();
                wait.until(ExpectedConditions.invisibilityOf(cookieAccept));
                log("Accepted cookie banner");
            } catch (Exception e) {
                log("No cookie banner present");
            }

            // Fill in login form
            WebElement usernameInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("username"))
            );
            WebElement passwordInput = driver.findElement(By.id("password"));
            usernameInput.sendKeys(reservationProperties.getUsername());
            passwordInput.sendKeys(reservationProperties.getPassword());
            log("Filled in login form");

            // Wait for login button to be clickable
            WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.name("login"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginButton);
            loginButton.click();
            log("Clicked login button");

            // Check for login success: look for logout link or user info
            boolean loginSuccess = false;
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(text(),'Uitloggen') or contains(text(),'Log uit') or contains(text(),'Logout') or contains(@href,'logout') or contains(text(),'Mijn account')]")
                ));
                loginSuccess = true;
            } catch (Exception e) {
                loginSuccess = false;
            }
            if (loginSuccess) {
                logger.info("Login successful for user: {}", reservationProperties.getUsername());
                log("Login successful");
            } else {
                logger.error("Login failed for user: {}", reservationProperties.getUsername());
                log("Login failed");
                return slots;
            }

            // Click on "Veld reserveren" (Field reservation) button or link
            try {
                WebElement veldButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class,'elementor-button-text') and contains(translate(normalize-space(text()), '\u00A0', ' '), 'Veld reserveren')]/..")
                ));
                veldButton.click();
                log("Clicked 'Veld reserveren' button");
            } catch (Exception e) {
                log("'Veld reserveren' button not found, navigating directly to booking page");
            }

            // Navigate to the booking page
            driver.get(reservationProperties.getBookingUrl());
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            log("Navigated to booking page: " + reservationProperties.getBookingUrl());

            // Scrape for available fields (Veld) on Wednesdays (wo)
            List<Map<String, String>> wednesdaySlots = new ArrayList<>();
            List<WebElement> fieldElements = driver.findElements(By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'veld')]"));
            for (WebElement field : fieldElements) {
                String fieldText = field.getText();
                WebElement parent = field.findElement(By.xpath("ancestor::*[contains(.,'wo')][1]"));
                if (parent != null && parent.getText().toLowerCase().contains("wo")) {
                    String parentText = parent.getText();
                    String day = "wo";
                    String time = "";
                    java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d{1,2}[:.]\\d{2})").matcher(parentText);
                    if (m.find()) {
                        time = m.group(1);
                    }
                    Map<String, String> slotInfo = new HashMap<>();
                    slotInfo.put("field", fieldText);
                    slotInfo.put("day", day);
                    slotInfo.put("time", time);
                    wednesdaySlots.add(slotInfo);
                    log("Available on Wednesday: " + fieldText + " at " + time);
                }
            }
            if (wednesdaySlots.isEmpty()) {
                log("No available fields found for Wednesday (wo)");
            }
            return wednesdaySlots;
        } catch (Exception e) {
            e.printStackTrace();
            log("Error: " + e.getMessage());
        } finally {
            driver.quit();
            log("Closed browser session");
        }
        return slots;
    }

    public static List<String> getLogMessages() {
        return new ArrayList<>(logMessages);
    }

    private void log(String message) {
        logger.info(message);
        logMessages.add(message);
    }

    public static void resetSearchAndLogs() {
        logMessages.clear();
    }
} 