package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optimizers.backend.dto.response.DashboardSummaryDTO;
import com.optimizers.backend.dto.response.RecentDeliveryDTO;
import com.optimizers.backend.dto.response.RecentRouteDTO;
import com.optimizers.backend.dto.response.StatusCountDTO;
import com.optimizers.backend.dto.response.TodayPerformanceDTO;
import com.optimizers.backend.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    @GetMapping("/delivery-status")
    public ResponseEntity<List<StatusCountDTO>> getDeliveryStatusCounts() {
        return ResponseEntity.ok(dashboardService.getDeliveryStatusCounts());
    }

    @GetMapping("/route-status")
    public ResponseEntity<List<StatusCountDTO>> getRouteStatusCounts() {
        return ResponseEntity.ok(dashboardService.getRouteStatusCounts());
    }

    @GetMapping("/recent-routes")
    public ResponseEntity<List<RecentRouteDTO>> getRecentRoutes() {
        return ResponseEntity.ok(dashboardService.getRecentRoutes());
    }

    @GetMapping("/recent-deliveries")
    public ResponseEntity<List<RecentDeliveryDTO>> getRecentDeliveries() {
        return ResponseEntity.ok(dashboardService.getRecentDeliveries());
    }

    @GetMapping("/today-performance")
    public ResponseEntity<TodayPerformanceDTO> getTodayPerformance() {
        return ResponseEntity.ok(dashboardService.getTodayPerformance());
    }
}