package com.example.reservation.controller;

import com.example.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/available-slots")
    public List<Map<String, String>> getAvailableSlots() {
        return reservationService.getAvailableSlots();
    }

    @GetMapping("/logs")
    public List<String> getLogs() {
        return ReservationService.getLogMessages();
    }

    @PostMapping("/reset")
    public void resetSearchAndLogs() {
        ReservationService.resetSearchAndLogs();
    }
} 