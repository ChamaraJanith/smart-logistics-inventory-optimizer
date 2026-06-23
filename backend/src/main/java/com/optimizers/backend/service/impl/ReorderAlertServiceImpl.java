// service/impl/ReorderAlertServiceImpl.java
package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.dto.response.RouteResponseDTO;
import com.optimizers.backend.entity.InventoryStock;
import com.optimizers.backend.entity.InventoryItem;
import com.optimizers.backend.entity.ReorderAlert;
import com.optimizers.backend.entity.Route;
import com.optimizers.backend.entity.Delivery;
import com.optimizers.backend.entity.DeliveryItem;
import com.optimizers.backend.entity.RouteDelivery;
import com.optimizers.backend.entity.Vehicle;
import com.optimizers.backend.entity.Warehouse;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.ReorderAlertRepository;
import com.optimizers.backend.repository.RouteRepository;
import com.optimizers.backend.repository.DeliveryRepository;
import com.optimizers.backend.repository.DeliveryItemRepository;
import com.optimizers.backend.repository.RouteDeliveryRepository;
import com.optimizers.backend.repository.VehicleRepository;
import com.optimizers.backend.repository.InventoryStockRepository;
import com.optimizers.backend.service.ReorderAlertService;
import com.optimizers.backend.service.DemandForecastService;

@Service
public class ReorderAlertServiceImpl implements ReorderAlertService {

    @Autowired
    private ReorderAlertRepository alertRepository;

    @Autowired
    private DemandForecastService demandForecastService;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private DeliveryItemRepository deliveryItemRepository;

    @Autowired
    private RouteDeliveryRepository routeDeliveryRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private InventoryStockRepository inventoryStockRepository;

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
        
        // Get predicted demand for next 7 days
        BigDecimal predicted7d = demandForecastService.getPredictedDemand7d(
        stock.getItem().getItemId(),
        stock.getWarehouse().getWarehouseId());

        // Create new alert
        ReorderAlert alert = new ReorderAlert();
        alert.setStock(stock);
        alert.setItem(stock.getItem());
        alert.setWarehouse(stock.getWarehouse());
        alert.setCurrentStock(available);
        alert.setReorderLevel(stock.getReorderLevel());
        alert.setSuggestedReorderQty(stock.getReorderQuantity());
        // Add AI predicted demand
        alert.setPredictedDemand7d(predicted7d);
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
        dto.setPredictedDemand7d(alert.getPredictedDemand7d());
        return dto;
    }

    @Override
    @Transactional
    public RouteResponseDTO planReplenishment(Integer alertId) {
        ReorderAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Reorder alert not found: " + alertId));

        InventoryStock targetStock = alert.getStock();
        Warehouse targetWarehouse = alert.getWarehouse();
        InventoryItem targetItem = alert.getItem();
        BigDecimal suggestedQty = alert.getSuggestedReorderQty() != null 
                ? alert.getSuggestedReorderQty() : targetStock.getReorderQuantity();

        // 1. Find source warehouse: stock of the same item name in other warehouses, with maximum stock available
        List<InventoryStock> potentialStocks = inventoryStockRepository.findAll().stream()
                .filter(s -> s.getItem().getItemName().equalsIgnoreCase(targetItem.getItemName())
                        && !s.getWarehouse().getWarehouseId().equals(targetWarehouse.getWarehouseId())
                        && s.getAvailableQuantity().compareTo(suggestedQty) >= 0)
                .sorted((s1, s2) -> s2.getAvailableQuantity().compareTo(s1.getAvailableQuantity()))
                .collect(Collectors.toList());

        Warehouse sourceWarehouse;
        InventoryItem sourceItem;
        if (!potentialStocks.isEmpty()) {
            sourceWarehouse = potentialStocks.get(0).getWarehouse();
            sourceItem = potentialStocks.get(0).getItem();
        } else {
            // fallback: find any other warehouse that exists
            List<InventoryStock> fallbackStocks = inventoryStockRepository.findAll().stream()
                    .filter(s -> !s.getWarehouse().getWarehouseId().equals(targetWarehouse.getWarehouseId()))
                    .collect(Collectors.toList());
            if (fallbackStocks.isEmpty()) {
                throw new IllegalArgumentException("No other warehouses found to source replenishment.");
            }
            sourceWarehouse = fallbackStocks.get(0).getWarehouse();
            sourceItem = fallbackStocks.get(0).getItem();
        }

        // 2. Find a vehicle at the source warehouse, or fallback to any vehicle
        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .filter(v -> v.getCurrentWarehouse() != null 
                        && v.getCurrentWarehouse().getWarehouseId().equals(sourceWarehouse.getWarehouseId())
                        && "AVAILABLE".equalsIgnoreCase(v.getCurrentStatus()))
                .collect(Collectors.toList());

        Vehicle vehicle;
        if (!vehicles.isEmpty()) {
            vehicle = vehicles.get(0);
        } else {
            List<Vehicle> allVehicles = vehicleRepository.findAll();
            if (allVehicles.isEmpty()) {
                throw new IllegalArgumentException("No vehicles available for replenishment.");
            }
            vehicle = allVehicles.get(0);
        }

        // 3. Create Delivery
        Delivery delivery = new Delivery();
        delivery.setWarehouseId(sourceWarehouse.getWarehouseId()); // start dispatch at source warehouse
        delivery.setCustomerName("Replenishment: " + targetWarehouse.getName());
        delivery.setContactNumber(targetWarehouse.getContactNumber());
        delivery.setDeliveryAddress(targetWarehouse.getAddress());
        delivery.setLatitude(targetWarehouse.getLatitude() != null ? targetWarehouse.getLatitude() : BigDecimal.ZERO);
        delivery.setLongitude(targetWarehouse.getLongitude() != null ? targetWarehouse.getLongitude() : BigDecimal.ZERO);
        delivery.setPackageWeight(targetItem.getUnitWeightKg() != null 
                ? targetItem.getUnitWeightKg().multiply(suggestedQty) : BigDecimal.ZERO);
        delivery.setPackageVolume(targetItem.getUnitVolume() != null 
                ? targetItem.getUnitVolume().multiply(suggestedQty) : BigDecimal.ZERO);
        delivery.setPriority("HIGH");
        delivery.setStatus("PENDING");
        delivery.setRequestedDate(LocalDateTime.now().plusDays(2));
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // 4. Create DeliveryItem
        DeliveryItem deliveryItem = new DeliveryItem();
        deliveryItem.setDelivery(savedDelivery);
        deliveryItem.setItem(sourceItem);
        deliveryItem.setQuantity(suggestedQty);
        deliveryItem.setUnitPrice(sourceItem.getUnitPrice());
        deliveryItemRepository.save(deliveryItem);

        // 5. Create Route
        Route route = new Route();
        route.setVehicle(vehicle);
        route.setRouteDate(java.time.LocalDate.now().plusDays(1));
        route.setStartWarehouseId(sourceWarehouse.getWarehouseId());
        route.setStartLocation(sourceWarehouse.getName() + " (" + sourceWarehouse.getAddress() + ")");
        route.setEndLocation(targetWarehouse.getName() + " (" + targetWarehouse.getAddress() + ")");
        route.setTotalDistanceKm(new BigDecimal("25.5")); // default estimation
        route.setEstimatedDurationMin(new BigDecimal("45.0"));
        route.setPredictedCost(new BigDecimal("150.0"));
        route.setPredictedDelayRisk(BigDecimal.ZERO);
        route.setOptimizationScore(new BigDecimal("95.0"));
        route.setRouteStatus("PLANNED");
        Route savedRoute = routeRepository.save(route);

        // 6. Assign Delivery to Route (RouteDelivery stop)
        RouteDelivery rd = new RouteDelivery();
        rd.setRoute(savedRoute);
        rd.setDelivery(savedDelivery);
        rd.setStopSequence(1);
        rd.setStopStatus("PENDING");
        routeDeliveryRepository.save(rd);

        // 7. Resolve the alert
        alert.setStatus("RESOLVED");
        alert.setResolvedAt(LocalDateTime.now());
        alertRepository.save(alert);

        // 8. Return RouteResponseDTO
        RouteResponseDTO responseDTO = new RouteResponseDTO();
        responseDTO.setRouteId(savedRoute.getRouteId());
        responseDTO.setVehicleId(vehicle.getVehicleId());
        responseDTO.setVehicleNumber(vehicle.getVehicleNumber());
        responseDTO.setRouteDate(savedRoute.getRouteDate());
        responseDTO.setStartWarehouseId(sourceWarehouse.getWarehouseId());
        responseDTO.setStartLocation(savedRoute.getStartLocation());
        responseDTO.setEndLocation(savedRoute.getEndLocation());
        responseDTO.setTotalDistanceKm(savedRoute.getTotalDistanceKm());
        responseDTO.setEstimatedDurationMin(savedRoute.getEstimatedDurationMin());
        responseDTO.setPredictedCost(savedRoute.getPredictedCost());
        responseDTO.setPredictedDelayRisk(savedRoute.getPredictedDelayRisk());
        responseDTO.setOptimizationScore(savedRoute.getOptimizationScore());
        responseDTO.setRouteStatus(savedRoute.getRouteStatus());
        responseDTO.setCreatedAt(savedRoute.getCreatedAt());
        return responseDTO;
    }
}