// service/AnomalyDetectionService.java
package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.response.AnomalyLogResponseDTO;
import com.optimizers.backend.entity.InventoryStock;
import com.optimizers.backend.entity.StockTransaction;

public interface AnomalyDetectionService {

    /**
     * Analyzes a stock transaction immediately after it is saved.
     * Called automatically from InventoryStockServiceImpl whenever stock is updated.
     *
     * @param stock       the current stock record after the update
     * @param transaction the transaction that was just performed
     */
    void analyzeStockTransaction(
            InventoryStock stock, StockTransaction transaction);

    // All open anomalies
    List<AnomalyLogResponseDTO> getAllOpenAnomalies();

    // By item
    List<AnomalyLogResponseDTO> getAnomaliesByItem(Integer itemId);

    // By warehouse
    List<AnomalyLogResponseDTO> getAnomaliesByWarehouse(
            Integer warehouseId);

    // Resolve
    AnomalyLogResponseDTO resolveAnomaly(Integer anomalyId);
}