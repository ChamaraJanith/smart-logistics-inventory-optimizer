// controller/InventoryDashboardController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.response.AnomalyLogResponseDTO;
import com.optimizers.backend.dto.response.InventoryDashboardSummaryDTO;
import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.dto.response.StatusCountDTO;
import com.optimizers.backend.dto.response.WarehouseStockSummaryDTO;
import com.optimizers.backend.service.InventoryDashboardService;

@RestController
@RequestMapping("/api/v1/inventory-dashboard")
@CrossOrigin(origins = "*")
public class InventoryDashboardController {

    @Autowired
    private InventoryDashboardService dashboardService;

    // Overall inventory summary
    @GetMapping("/summary")
    public ResponseEntity<InventoryDashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(
                dashboardService.getInventorySummary());
    }

    // Per warehouse stock overview
    @GetMapping("/warehouse-summary")
    public ResponseEntity<List<WarehouseStockSummaryDTO>>
            getWarehouseSummary() {
        return ResponseEntity.ok(
                dashboardService.getWarehouseStockSummary());
    }

    // Stock status counts
    @GetMapping("/stock-status-counts")
    public ResponseEntity<List<StatusCountDTO>>
            getStockStatusCounts() {
        return ResponseEntity.ok(
                dashboardService.getStockStatusCounts());
    }

    // Latest 5 open reorder alerts
    @GetMapping("/recent-alerts")
    public ResponseEntity<List<ReorderAlertResponseDTO>>
            getRecentAlerts() {
        return ResponseEntity.ok(
                dashboardService.getRecentAlerts());
    }

    // Latest 5 open anomalies
    @GetMapping("/recent-anomalies")
    public ResponseEntity<List<AnomalyLogResponseDTO>>
            getRecentAnomalies() {
        return ResponseEntity.ok(
                dashboardService.getRecentAnomalies());
    }
}