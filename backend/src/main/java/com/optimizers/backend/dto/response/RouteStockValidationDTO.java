package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class RouteStockValidationDTO {
    private List<RouteStockItemValidationDTO> items;
    private boolean hasShortage;
    private BigDecimal totalWeightKg;
    private BigDecimal totalVolume;
    private BigDecimal vehicleWeightCapacity;
    private BigDecimal vehicleVolumeCapacity;

    public RouteStockValidationDTO() {}

    public RouteStockValidationDTO(List<RouteStockItemValidationDTO> items, boolean hasShortage,
                                   BigDecimal totalWeightKg, BigDecimal totalVolume,
                                   BigDecimal vehicleWeightCapacity, BigDecimal vehicleVolumeCapacity) {
        this.items = items;
        this.hasShortage = hasShortage;
        this.totalWeightKg = totalWeightKg;
        this.totalVolume = totalVolume;
        this.vehicleWeightCapacity = vehicleWeightCapacity;
        this.vehicleVolumeCapacity = vehicleVolumeCapacity;
    }

    public List<RouteStockItemValidationDTO> getItems() {
        return items;
    }

    public void setItems(List<RouteStockItemValidationDTO> items) {
        this.items = items;
    }

    public boolean getHasShortage() {
        return hasShortage;
    }

    public void setHasShortage(boolean hasShortage) {
        this.hasShortage = hasShortage;
    }

    public BigDecimal getTotalWeightKg() {
        return totalWeightKg;
    }

    public void setTotalWeightKg(BigDecimal totalWeightKg) {
        this.totalWeightKg = totalWeightKg;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getVehicleWeightCapacity() {
        return vehicleWeightCapacity;
    }

    public void setVehicleWeightCapacity(BigDecimal vehicleWeightCapacity) {
        this.vehicleWeightCapacity = vehicleWeightCapacity;
    }

    public BigDecimal getVehicleVolumeCapacity() {
        return vehicleVolumeCapacity;
    }

    public void setVehicleVolumeCapacity(BigDecimal vehicleVolumeCapacity) {
        this.vehicleVolumeCapacity = vehicleVolumeCapacity;
    }
}
