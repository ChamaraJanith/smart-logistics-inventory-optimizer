// service/impl/InventoryDashboardServiceImpl.java
package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.response.AnomalyLogResponseDTO;
import com.optimizers.backend.dto.response.InventoryDashboardSummaryDTO;
import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.dto.response.StatusCountDTO;
import com.optimizers.backend.dto.response.WarehouseStockSummaryDTO;
import com.optimizers.backend.entity.AnomalyLog;
import com.optimizers.backend.entity.ReorderAlert;
import com.optimizers.backend.entity.Warehouse;
import com.optimizers.backend.repository.AnomalyLogRepository;
import com.optimizers.backend.repository.InventoryItemRepository;
import com.optimizers.backend.repository.InventoryStockRepository;
import com.optimizers.backend.repository.ReorderAlertRepository;
import com.optimizers.backend.repository.StockTransactionRepository;
import com.optimizers.backend.repository.WarehouseRepository;
import com.optimizers.backend.service.InventoryDashboardService;

@Service
public class InventoryDashboardServiceImpl
        implements InventoryDashboardService {

    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private InventoryItemRepository itemRepository;
    @Autowired private InventoryStockRepository stockRepository;
    @Autowired private ReorderAlertRepository alertRepository;
    @Autowired private AnomalyLogRepository anomalyRepository;
    @Autowired private StockTransactionRepository transactionRepository;

    @Override
    public InventoryDashboardSummaryDTO getInventorySummary() {

        // Today start and end timestamps
        LocalDateTime startOfDay =
                LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay =
                LocalDate.now().plusDays(1)
                        .atStartOfDay().minusNanos(1);

        // Active warehouses count
        long totalWarehouses =
                warehouseRepository.countByStatus("ACTIVE");

        // Active items count
        long totalItems =
                itemRepository.countByStatus("ACTIVE");

        // Low stock items count (available <= reorder level)
        long lowStockCount =
                stockRepository.findLowStockItems().size();

        // Out of stock items count (available <= 0)
        long outOfStockCount =
                stockRepository.countOutOfStockItems();

        // Open reorder alerts count
        long openAlertsCount =
                alertRepository.countByStatus("OPEN");

        // Critical severity open alerts
        long criticalAlertsCount =
                alertRepository.countBySeverityAndStatus(
                        "CRITICAL", "OPEN");

        // Open anomalies count
        long openAnomaliesCount =
                anomalyRepository.countByStatus("OPEN");

        // Today's stock transactions count
        long todayTransactions =
                transactionRepository.countTodayTransactions(
                        startOfDay, endOfDay);

        return new InventoryDashboardSummaryDTO(
                totalWarehouses,
                totalItems,
                lowStockCount,
                outOfStockCount,
                openAlertsCount,
                criticalAlertsCount,
                openAnomaliesCount,
                todayTransactions
        );
    }

    @Override
    public List<WarehouseStockSummaryDTO> getWarehouseStockSummary() {

        // Get all active warehouses
        return warehouseRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::buildWarehouseSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<StatusCountDTO> getStockStatusCounts() {

        // Count all stock items by status
        long lowStock = stockRepository.findLowStockItems().size();
        long outOfStock = stockRepository.countOutOfStockItems();

        // Total active items
        long totalActive = itemRepository.countByStatus("ACTIVE");

        // Normal = total - low - out of stock
        long normalStock = totalActive - lowStock - outOfStock;
        if (normalStock < 0) normalStock = 0;

        return Arrays.asList(
                new StatusCountDTO("NORMAL", normalStock),
                new StatusCountDTO("LOW", lowStock),
                new StatusCountDTO("OUT_OF_STOCK", outOfStock)
        );
    }

    @Override
    public List<ReorderAlertResponseDTO> getRecentAlerts() {

        // Get latest 5 open reorder alerts
        return alertRepository
                .findTop5ByStatusOrderByTriggeredAtDesc("OPEN")
                .stream()
                .map(this::mapAlertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnomalyLogResponseDTO> getRecentAnomalies() {

        // Get latest 5 open anomalies
        return anomalyRepository
                .findTop5ByStatusOrderByDetectedAtDesc("OPEN")
                .stream()
                .map(this::mapAnomalyToDTO)
                .collect(Collectors.toList());
    }

    // Build per-warehouse stock summary
    private WarehouseStockSummaryDTO buildWarehouseSummary(
            Warehouse warehouse) {

        Integer wId = warehouse.getWarehouseId();

        WarehouseStockSummaryDTO dto = new WarehouseStockSummaryDTO();
        dto.setWarehouseId(wId);
        dto.setWarehouseName(warehouse.getName());
        dto.setWarehouseStatus(warehouse.getStatus());

        // Total active items in this warehouse
        dto.setTotalItems(
                itemRepository
                        .countByWarehouse_WarehouseIdAndStatus(
                                wId, "ACTIVE"));

        // Stock status counts for this warehouse
        dto.setNormalStockCount(
                stockRepository.countNormalStockByWarehouse(wId));
        dto.setLowStockCount(
                stockRepository.countLowStockByWarehouse(wId));
        dto.setCriticalStockCount(
                stockRepository.countCriticalStockByWarehouse(wId));
        dto.setOutOfStockCount(
                stockRepository.countOutOfStockByWarehouse(wId));

        // Open alerts for this warehouse
        dto.setOpenAlertsCount(
                alertRepository
                        .countByWarehouse_WarehouseIdAndStatus(
                                wId, "OPEN"));

        return dto;
    }

    // Map ReorderAlert entity to response DTO
    private ReorderAlertResponseDTO mapAlertToDTO(ReorderAlert alert) {
        ReorderAlertResponseDTO dto = new ReorderAlertResponseDTO();
        dto.setAlertId(alert.getAlertId());
        dto.setStockId(alert.getStock().getStockId());
        dto.setItemId(alert.getItem().getItemId());
        dto.setItemName(alert.getItem().getItemName());
        dto.setSku(alert.getItem().getSku());
        dto.setWarehouseId(alert.getWarehouse().getWarehouseId());
        dto.setWarehouseName(alert.getWarehouse().getName());
        dto.setAlertType(alert.getAlertType());
        dto.setCurrentStock(alert.getCurrentStock());
        dto.setReorderLevel(alert.getReorderLevel());
        dto.setSuggestedReorderQty(alert.getSuggestedReorderQty());
        dto.setSeverity(alert.getSeverity());
        dto.setStatus(alert.getStatus());
        dto.setTriggeredAt(alert.getTriggeredAt());
        dto.setResolvedAt(alert.getResolvedAt());
        return dto;
    }

    // Map AnomalyLog entity to response DTO
    private AnomalyLogResponseDTO mapAnomalyToDTO(AnomalyLog log) {
        AnomalyLogResponseDTO dto = new AnomalyLogResponseDTO();
        dto.setAnomalyId(log.getAnomalyId());
        if (log.getItem() != null) {
            dto.setItemId(log.getItem().getItemId());
            dto.setItemName(log.getItem().getItemName());
            dto.setSku(log.getItem().getSku());
        }
        if (log.getWarehouse() != null) {
            dto.setWarehouseId(log.getWarehouse().getWarehouseId());
            dto.setWarehouseName(log.getWarehouse().getName());
        }
        dto.setAnomalyType(log.getAnomalyType());
        dto.setDescription(log.getDescription());
        dto.setDeviationScore(log.getDeviationScore());
        dto.setStatus(log.getStatus());
        dto.setDetectedAt(log.getDetectedAt());
        dto.setResolvedAt(log.getResolvedAt());
        return dto;
    }
}