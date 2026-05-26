// service/impl/DemandForecastServiceImpl.java
package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optimizers.backend.dto.request.DemandForecastRequestDTO;
import com.optimizers.backend.dto.response.DemandForecastResponseDTO;
import com.optimizers.backend.entity.DemandForecast;
import com.optimizers.backend.entity.InventoryItem;
import com.optimizers.backend.entity.InventoryStock;
import com.optimizers.backend.entity.StockTransaction;
import com.optimizers.backend.entity.Warehouse;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.DemandForecastRepository;
import com.optimizers.backend.repository.InventoryItemRepository;
import com.optimizers.backend.repository.InventoryStockRepository;
import com.optimizers.backend.repository.StockTransactionRepository;
import com.optimizers.backend.repository.WarehouseRepository;
import com.optimizers.backend.service.DemandForecastService;

@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    // Current AI model version
    private static final String MODEL_VERSION = "v1.0-moving-avg";

    @Autowired
    private DemandForecastRepository forecastRepository;

    @Autowired
    private InventoryItemRepository itemRepository;

    @Autowired
    private InventoryStockRepository stockRepository;

    @Autowired
    private StockTransactionRepository transactionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    // Generate forecast for a single item in a warehouse
    @Override
    @Transactional
    public List<DemandForecastResponseDTO> generateForecast(
            DemandForecastRequestDTO requestDTO) {

        // Find inventory item
        InventoryItem item = itemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item not found: " + requestDTO.getItemId()));

        // Find warehouse
        Warehouse warehouse = warehouseRepository.findById(requestDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found: " + requestDTO.getWarehouseId()));

        // Default forecast period = 7 days
        int forecastDays = requestDTO.getForecastDays() != null
                ? requestDTO.getForecastDays()
                : 7;

        return generateForecastInternal(item, warehouse, forecastDays);
    }

    // Generate forecast for all items in a warehouse
    @Override
    @Transactional
    public List<DemandForecastResponseDTO> generateForecastForWarehouse(
            Integer warehouseId, Integer forecastDays) {

        // Find warehouse
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found: " + warehouseId));

        // Get all items belonging to the warehouse
        List<InventoryItem> items = itemRepository
                .findByWarehouse_WarehouseId(warehouseId);

        List<DemandForecastResponseDTO> results = new ArrayList<>();

        // Generate forecast for each item
        for (InventoryItem item : items) {
            results.addAll(generateForecastInternal(
                    item,
                    warehouse,
                    forecastDays != null ? forecastDays : 7));
        }

        return results;
    }

    // Get forecast history for a specific item
    @Override
    public List<DemandForecastResponseDTO> getForecastByItem(Integer itemId) {

        return forecastRepository
                .findByItem_ItemIdOrderByForecastDateAsc(itemId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get forecast history for a warehouse
    @Override
    public List<DemandForecastResponseDTO> getForecastByWarehouse(
            Integer warehouseId) {

        return forecastRepository
                .findByWarehouse_WarehouseIdOrderByForecastDateAsc(warehouseId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get total predicted demand for next 7 days
    @Override
    public BigDecimal getPredictedDemand7d(
            Integer itemId,
            Integer warehouseId) {

        LocalDate today = LocalDate.now();

        // Get forecasts within next 7 days
        List<DemandForecast> forecasts = forecastRepository
                .findForecastsInRange(
                        itemId,
                        warehouseId,
                        today,
                        today.plusDays(6));

        // Sum all predicted demand values
        return forecasts.stream()
                .map(DemandForecast::getPredictedDemand)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(3, RoundingMode.HALF_UP);
    }

    // ================================================================
    // CORE AI FORECASTING LOGIC
    // Moving Average Based Demand Forecasting
    // ================================================================
    private List<DemandForecastResponseDTO> generateForecastInternal(
            InventoryItem item,
            Warehouse warehouse,
            int forecastDays) {

        // Step 1:
        // Retrieve DISPATCH transactions from last 90 days
        List<StockTransaction> dispatches = transactionRepository
                .findByItem_ItemIdOrderByCreatedAtDesc(item.getItemId())
                .stream()
                .filter(t -> t.getTransactionType().equals("DISPATCH"))
                .filter(t -> t.getCreatedAt().isAfter(
                        java.time.LocalDateTime.now().minusDays(90)))
                .collect(Collectors.toList());

        // Step 2:
        // Calculate average daily demand
        BigDecimal dailyAverage = calculateDailyAverage(dispatches);

        // Step 3:
        // Calculate confidence score based on amount of historical data
        BigDecimal confidence = calculateConfidence(dispatches.size());

        // Step 4:
        // Detect seasonal or demand pattern
        String seasonPattern = detectSeasonPattern(dispatches);

        // Step 5:
        // Generate future forecast records
        List<DemandForecastResponseDTO> results = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (int i = 1; i <= forecastDays; i++) {

            LocalDate forecastDate = today.plusDays(i);

            // Apply weekday/weekend demand adjustment
            BigDecimal adjustedDemand =
                    applyDayOfWeekAdjustment(dailyAverage, forecastDate);

            // Check whether forecast already exists
            DemandForecast forecast = forecastRepository
                    .findByItem_ItemIdAndWarehouse_WarehouseIdAndForecastDateAndModelVersion(
                            item.getItemId(),
                            warehouse.getWarehouseId(),
                            forecastDate,
                            MODEL_VERSION)
                    .orElse(new DemandForecast());

            // Set forecast values
            forecast.setItem(item);
            forecast.setWarehouse(warehouse);
            forecast.setForecastDate(forecastDate);
            forecast.setPredictedDemand(adjustedDemand);
            forecast.setConfidenceScore(confidence);
            forecast.setSeasonPattern(seasonPattern);
            forecast.setModelVersion(MODEL_VERSION);

            // Save forecast
            DemandForecast saved = forecastRepository.save(forecast);

            results.add(mapToResponseDTO(saved));
        }

        return results;
    }

    // Calculate average daily demand
    // Formula:
    // Total dispatched quantity / active transaction days
    private BigDecimal calculateDailyAverage(
            List<StockTransaction> dispatches) {

        // No history available
        if (dispatches.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Sum total dispatched quantity
        BigDecimal total = dispatches.stream()
                .map(StockTransaction::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Count unique active days
        long activeDays = dispatches.stream()
                .map(t -> t.getCreatedAt().toLocalDate())
                .distinct()
                .count();

        if (activeDays == 0) {
            return BigDecimal.ZERO;
        }

        // Calculate average
        return total.divide(
                BigDecimal.valueOf(activeDays),
                3,
                RoundingMode.HALF_UP);
    }

    // Calculate confidence score based on historical data volume
    private BigDecimal calculateConfidence(int dataPoints) {

        if (dataPoints >= 60) {
            return new BigDecimal("0.9000");
        }

        if (dataPoints >= 30) {
            return new BigDecimal("0.7500");
        }

        if (dataPoints >= 10) {
            return new BigDecimal("0.5000");
        }

        if (dataPoints >= 1) {
            return new BigDecimal("0.2500");
        }

        // Very low confidence when no history exists
        return new BigDecimal("0.1000");
    }

    // Detect demand stability pattern
    private String detectSeasonPattern(
            List<StockTransaction> dispatches) {

        if (dispatches.isEmpty()) {
            return "INSUFFICIENT_DATA";
        }

        if (dispatches.size() >= 60) {
            return "STABLE";
        }

        if (dispatches.size() >= 20) {
            return "MODERATE";
        }

        return "VOLATILE";
    }

    // Apply day-of-week adjustment
    // Weekend demand is lower
    // Monday demand is slightly higher
    private BigDecimal applyDayOfWeekAdjustment(
            BigDecimal dailyAverage,
            LocalDate date) {

        switch (date.getDayOfWeek()) {

            case SATURDAY:
            case SUNDAY:

                // Reduce weekend demand by 30%
                return dailyAverage
                        .multiply(new BigDecimal("0.70"))
                        .setScale(3, RoundingMode.HALF_UP);

            case MONDAY:

                // Increase Monday demand by 20%
                return dailyAverage
                        .multiply(new BigDecimal("1.20"))
                        .setScale(3, RoundingMode.HALF_UP);

            default:

                // Normal weekday demand
                return dailyAverage
                        .setScale(3, RoundingMode.HALF_UP);
        }
    }

    // Convert entity object into response DTO
    private DemandForecastResponseDTO mapToResponseDTO(
            DemandForecast f) {

        DemandForecastResponseDTO dto =
                new DemandForecastResponseDTO();

        dto.setForecastId(f.getForecastId());

        dto.setItemId(f.getItem().getItemId());
        dto.setItemName(f.getItem().getItemName());
        dto.setSku(f.getItem().getSku());

        dto.setWarehouseId(f.getWarehouse().getWarehouseId());
        dto.setWarehouseName(f.getWarehouse().getName());

        dto.setForecastDate(f.getForecastDate());

        dto.setPredictedDemand(f.getPredictedDemand());

        dto.setConfidenceScore(f.getConfidenceScore());

        dto.setSeasonPattern(f.getSeasonPattern());

        dto.setModelVersion(f.getModelVersion());

        dto.setCreatedAt(f.getCreatedAt());

        return dto;
    }
}