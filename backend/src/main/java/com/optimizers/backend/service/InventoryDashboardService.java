// service/InventoryDashboardService.java
package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.response.AnomalyLogResponseDTO;
import com.optimizers.backend.dto.response.InventoryDashboardSummaryDTO;
import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.dto.response.StatusCountDTO;
import com.optimizers.backend.dto.response.WarehouseStockSummaryDTO;

public interface InventoryDashboardService {

    // Overall inventory summary
    InventoryDashboardSummaryDTO getInventorySummary();

    // Per warehouse stock overview
    List<WarehouseStockSummaryDTO> getWarehouseStockSummary();

    // Stock status counts (NORMAL, LOW, CRITICAL, OUT_OF_STOCK)
    List<StatusCountDTO> getStockStatusCounts();

    // Latest 5 open reorder alerts
    List<ReorderAlertResponseDTO> getRecentAlerts();

    // Latest 5 open anomalies
    List<AnomalyLogResponseDTO> getRecentAnomalies();
}