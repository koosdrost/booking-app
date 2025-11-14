package com.example.reservation.controller;

import com.example.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/available-slots")
    public ResponseEntity<?> getAvailableSlots() {
        try {
            List<Map<String, String>> slots = reservationService.getAvailableSlots();
            Map<String, Object> response = new HashMap<>();
            response.put("slots", slots);
            response.put("count", slots.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching available slots", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch available slots: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        try {
            return ResponseEntity.ok(ReservationService.getLogMessages());
        } catch (Exception e) {
            logger.error("Error fetching logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetSearchAndLogs() {
        try {
            ReservationService.resetSearchAndLogs();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Search and logs reset successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error resetting search and logs", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to reset: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
} 