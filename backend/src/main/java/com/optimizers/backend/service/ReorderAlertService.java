// service/ReorderAlertService.java
package com.optimizers.backend.service;

import java.util.List;
import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.entity.InventoryStock;

import com.optimizers.backend.dto.response.RouteResponseDTO;

public interface ReorderAlertService {
    void checkAndTriggerAlert(InventoryStock stock); // called after stock update
    List<ReorderAlertResponseDTO> getAllOpenAlerts();
    List<ReorderAlertResponseDTO> getOpenAlertsByWarehouse(Integer warehouseId);
    List<ReorderAlertResponseDTO> getAlertsBySeverity(String severity);
    ReorderAlertResponseDTO resolveAlert(Integer alertId);
    RouteResponseDTO planReplenishment(Integer alertId);
}