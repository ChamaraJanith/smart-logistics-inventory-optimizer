// service/impl/ReorderAlertServiceImpl.java
package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.entity.InventoryStock;
import com.optimizers.backend.entity.ReorderAlert;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.ReorderAlertRepository;
import com.optimizers.backend.service.ReorderAlertService;

@Service
public class ReorderAlertServiceImpl implements ReorderAlertService {

    @Autowired
    private ReorderAlertRepository alertRepository;

    @Override
    public void checkAndTriggerAlert(InventoryStock stock) {
        BigDecimal available = stock.getAvailableQuantity();
        if (available == null) return;

        // Auto-resolve if stock back to normal
        if (available.compareTo(stock.getReorderLevel()) > 0) {
            Optional<ReorderAlert> existing = alertRepository
                    .findByStock_StockIdAndStatus(stock.getStockId(), "OPEN");
            existing.ifPresent(alert -> {
                alert.setStatus("RESOLVED");
                alert.setResolvedAt(LocalDateTime.now());
                alertRepository.save(alert);
            });
            return;
        }

        // Don't duplicate open alerts
        Optional<ReorderAlert> existing = alertRepository
                .findByStock_StockIdAndStatus(stock.getStockId(), "OPEN");
        if (existing.isPresent()) {
            // Update current stock on existing alert
            existing.get().setCurrentStock(available);
            existing.get().setSeverity(determineSeverity(stock));
            alertRepository.save(existing.get());
            return;
        }

        // Create new alert
        ReorderAlert alert = new ReorderAlert();
        alert.setStock(stock);
        alert.setItem(stock.getItem());
        alert.setWarehouse(stock.getWarehouse());
        alert.setCurrentStock(available);
        alert.setReorderLevel(stock.getReorderLevel());
        alert.setSuggestedReorderQty(stock.getReorderQuantity());
        alert.setSeverity(determineSeverity(stock));
        alert.setAlertType(available.compareTo(BigDecimal.ZERO) <= 0 ? "OUT_OF_STOCK" : "LOW_STOCK");
        alert.setStatus("OPEN");
        alertRepository.save(alert);
    }

    @Override
    public List<ReorderAlertResponseDTO> getAllOpenAlerts() {
        return alertRepository.findByStatus("OPEN").stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReorderAlertResponseDTO> getOpenAlertsByWarehouse(Integer warehouseId) {
        return alertRepository.findByWarehouse_WarehouseIdAndStatus(warehouseId, "OPEN").stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<ReorderAlertResponseDTO> getAlertsBySeverity(String severity) {
        return alertRepository.findBySeverityAndStatus(severity, "OPEN").stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ReorderAlertResponseDTO resolveAlert(Integer alertId) {
        ReorderAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + alertId));
        alert.setStatus("RESOLVED");
        alert.setResolvedAt(LocalDateTime.now());
        return mapToResponseDTO(alertRepository.save(alert));
    }

    private String determineSeverity(InventoryStock stock) {
        BigDecimal available = stock.getAvailableQuantity();
        if (available.compareTo(BigDecimal.ZERO) <= 0) return "CRITICAL";
        BigDecimal halfReorder = stock.getReorderLevel().multiply(new BigDecimal("0.5"));
        if (available.compareTo(halfReorder) <= 0) return "HIGH";
        return "MEDIUM";
    }

    private ReorderAlertResponseDTO mapToResponseDTO(ReorderAlert alert) {
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
}