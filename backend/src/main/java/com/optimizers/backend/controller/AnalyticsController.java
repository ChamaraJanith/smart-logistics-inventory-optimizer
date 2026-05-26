package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.optimizers.backend.dto.response.TrendDataDTO;
import com.optimizers.backend.service.AnalyticsService;

@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/delivery-trends")
    public ResponseEntity<List<TrendDataDTO>> getDeliveryTrends(
            @RequestParam(defaultValue = "weekly") String period) {
        return ResponseEntity.ok(analyticsService.getDeliveryTrends(period));
    }

    @GetMapping("/route-trends")
    public ResponseEntity<List<TrendDataDTO>> getRouteTrends(
            @RequestParam(defaultValue = "weekly") String period) {
        return ResponseEntity.ok(analyticsService.getRouteTrends(period));
    }
}