// service/impl/AnomalyDetectionServiceImpl.java
package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.response.AnomalyLogResponseDTO;
import com.optimizers.backend.entity.AnomalyLog;
import com.optimizers.backend.entity.InventoryStock;
import com.optimizers.backend.entity.StockTransaction;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.AnomalyLogRepository;
import com.optimizers.backend.repository.StockTransactionRepository;
import com.optimizers.backend.service.AnomalyDetectionService;

/**
 * Implementation of the Anomaly Detection AI module.
 *
 * Uses three rule-based detection algorithms:
 *   1. Large Dispatch   — dispatch quantity is 3x above the 30-day average
 *   2. Rapid Depletion  — a single dispatch removes 50%+ of available stock
 *   3. Critical Stock   — remaining stock falls below 10% of the reorder level
 */
@Service
public class AnomalyDetectionServiceImpl implements AnomalyDetectionService {

    @Autowired
    private AnomalyLogRepository anomalyLogRepository;

    @Autowired
    private StockTransactionRepository transactionRepository;

    // ================================================================
    // ENTRY POINT — Called after every stock update
    // Runs all three detection rules in sequence
    // ================================================================
    @Override
    public void analyzeStockTransaction(InventoryStock stock, StockTransaction transaction) {
        checkLargeDispatch(stock, transaction);
        checkRapidDepletion(stock, transaction);
        checkNegativeStockAttempt(stock, transaction);
    }

    // ================================================================
    // RULE 1: Large Dispatch Detection
    //
    // Algorithm:
    //   - Fetch all DISPATCH transactions for this item in the last 30 days
    //   - Calculate the average dispatch quantity
    //   - If the current dispatch is 3x or more the average → flag as anomaly
    //   - deviationScore = currentQuantity / averageQuantity
    //
    // Minimum 3 past transactions required to avoid false positives on new items
    // ================================================================
    private void checkLargeDispatch(InventoryStock stock, StockTransaction transaction) {

        // Only apply this rule to DISPATCH transactions
        if (!transaction.getTransactionType().equals("DISPATCH")) return;

        // Load all dispatch transactions for this item from the last 30 days
        List<StockTransaction> recentDispatches =
                transactionRepository
                        .findByItem_ItemIdOrderByCreatedAtDesc(stock.getItem().getItemId())
                        .stream()
                        .filter(t -> t.getTransactionType().equals("DISPATCH"))
                        .filter(t -> t.getCreatedAt().isAfter(LocalDateTime.now().minusDays(30)))
                        .collect(Collectors.toList());

        // Skip if there is not enough history to calculate a meaningful average
        if (recentDispatches.size() < 3) return;

        // Sum all recent dispatch quantities and divide to get the average
        BigDecimal totalDispatched = recentDispatches.stream()
                .map(StockTransaction::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgDispatch = totalDispatched.divide(
                BigDecimal.valueOf(recentDispatches.size()), 3, RoundingMode.HALF_UP);

        // Avoid division by zero
        if (avgDispatch.compareTo(BigDecimal.ZERO) == 0) return;

        // deviationScore = how many times larger this dispatch is compared to the average
        BigDecimal deviationScore = transaction.getQuantity()
                .divide(avgDispatch, 4, RoundingMode.HALF_UP);

        // If this dispatch is 3x or more the average, it is abnormal
        if (deviationScore.compareTo(new BigDecimal("3.0")) >= 0) {
            createAnomaly(
                    stock,
                    "LARGE_DISPATCH",
                    String.format(
                            "Unusually large dispatch detected. " +
                            "Quantity: %.3f, Average: %.3f, Deviation: %.2fx",
                            transaction.getQuantity(), avgDispatch, deviationScore),
                    deviationScore
            );
        }
    }

    // ================================================================
    // RULE 2: Rapid Stock Depletion Detection
    //
    // Algorithm:
    //   - Calculate what percentage of the pre-transaction stock was dispatched
    //   - If the drop is 50% or more in a single transaction → flag as anomaly
    //   - deviationScore = dropPercentage / 100
    //
    // Example: Stock was 100, dispatched 60 → 60% drop → ANOMALY
    // ================================================================
    private void checkRapidDepletion(InventoryStock stock, StockTransaction transaction) {

        // Only apply this rule to DISPATCH transactions
        if (!transaction.getTransactionType().equals("DISPATCH")) return;

        BigDecimal quantityBefore = transaction.getQuantityBefore();

        // Avoid division by zero (no stock before this transaction)
        if (quantityBefore.compareTo(BigDecimal.ZERO) == 0) return;

        // Calculate what percentage of the stock was removed in this single transaction
        BigDecimal dropPercentage = transaction.getQuantity()
                .divide(quantityBefore, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        // If 50% or more was removed in one transaction, flag it
        if (dropPercentage.compareTo(new BigDecimal("50")) >= 0) {
            BigDecimal deviationScore = dropPercentage
                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

            createAnomaly(
                    stock,
                    "RAPID_DEPLETION",
                    String.format(
                            "Stock dropped by %.1f%% in single transaction. " +
                            "Before: %.3f, Dispatched: %.3f",
                            dropPercentage, quantityBefore, transaction.getQuantity()),
                    deviationScore
            );
        }
    }

    // ================================================================
    // RULE 3: Critical Stock Level Detection
    //
    // Algorithm:
    //   - After a DISPATCH, check the remaining available quantity
    //   - If available stock is less than 10% of the reorder level → flag as anomaly
    //   - This catches situations where stock is dangerously close to zero
    //   - deviationScore is fixed at 0.95 (very high severity)
    // ================================================================
    private void checkNegativeStockAttempt(InventoryStock stock, StockTransaction transaction) {

        // Only apply this rule to DISPATCH transactions
        if (!transaction.getTransactionType().equals("DISPATCH")) return;

        BigDecimal available = stock.getAvailableQuantity();
        if (available == null) return;

        // Calculate 10% of the reorder level as the critical threshold
        BigDecimal tenPercent = stock.getReorderLevel()
                .multiply(new BigDecimal("0.1"));

        // If available stock is at or below the critical threshold, flag it
        if (available.compareTo(tenPercent) <= 0 && available.compareTo(BigDecimal.ZERO) >= 0) {
            createAnomaly(
                    stock,
                    "CRITICAL_STOCK_LEVEL",
                    String.format(
                            "Stock critically low after dispatch. " +
                            "Available: %.3f units remaining.",
                            available),
                    new BigDecimal("0.9500")
            );
        }
    }

    // ================================================================
    // HELPER — Creates and saves a new AnomalyLog record
    // ================================================================
    private void createAnomaly(InventoryStock stock, String type,
                                String description, BigDecimal deviationScore) {
        AnomalyLog anomaly = new AnomalyLog();
        anomaly.setItem(stock.getItem());
        anomaly.setWarehouse(stock.getWarehouse());
        anomaly.setAnomalyType(type);
        anomaly.setDescription(description);
        anomaly.setDeviationScore(deviationScore);
        anomaly.setStatus("OPEN");
        anomalyLogRepository.save(anomaly);
    }

    // ================================================================
    // QUERY METHODS
    // ================================================================

    /** Returns all anomalies that have not yet been resolved. */
    @Override
    public List<AnomalyLogResponseDTO> getAllOpenAnomalies() {
        return anomalyLogRepository.findByStatus("OPEN")
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /** Returns all anomalies for a specific inventory item, newest first. */
    @Override
    public List<AnomalyLogResponseDTO> getAnomaliesByItem(Integer itemId) {
        return anomalyLogRepository
                .findByItem_ItemIdOrderByDetectedAtDesc(itemId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /** Returns all anomalies detected in a specific warehouse, newest first. */
    @Override
    public List<AnomalyLogResponseDTO> getAnomaliesByWarehouse(Integer warehouseId) {
        return anomalyLogRepository
                .findByWarehouse_WarehouseIdOrderByDetectedAtDesc(warehouseId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marks an anomaly as resolved and records the time it was resolved.
     * Throws ResourceNotFoundException if the anomaly ID does not exist.
     */
    @Override
    public AnomalyLogResponseDTO resolveAnomaly(Integer anomalyId) {
        AnomalyLog anomaly = anomalyLogRepository.findById(anomalyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Anomaly not found: " + anomalyId));

        anomaly.setStatus("RESOLVED");
        anomaly.setResolvedAt(LocalDateTime.now());
        return mapToResponseDTO(anomalyLogRepository.save(anomaly));
    }

    // ================================================================
    // MAPPER — Converts AnomalyLog entity → AnomalyLogResponseDTO
    // Safely handles null item and warehouse references
    // ================================================================
    private AnomalyLogResponseDTO mapToResponseDTO(AnomalyLog log) {
        AnomalyLogResponseDTO dto = new AnomalyLogResponseDTO();
        dto.setAnomalyId(log.getAnomalyId());

        // Map item fields only if the item reference is not null
        if (log.getItem() != null) {
            dto.setItemId(log.getItem().getItemId());
            dto.setItemName(log.getItem().getItemName());
            dto.setSku(log.getItem().getSku());
        }

        // Map warehouse fields only if the warehouse reference is not null
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