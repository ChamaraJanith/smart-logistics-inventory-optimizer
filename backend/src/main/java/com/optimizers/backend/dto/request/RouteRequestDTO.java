package com.optimizers.backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class RouteRequestDTO {

    @NotNull(message = "Vehicle ID is required")
    private Integer vehicleId;

    private Integer driverId;

    @NotNull(message = "Route date is required")
    private LocalDate routeDate;

    private Integer startWarehouseId;
    private String startLocation;
    private String endLocation;
    private BigDecimal totalDistanceKm;
    private BigDecimal estimatedDurationMin;
    private BigDecimal predictedCost;
    private BigDecimal predictedDelayRisk;
    private BigDecimal optimizationScore;
    private String routeStatus;

    public RouteRequestDTO() {
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public LocalDate getRouteDate() {
        return routeDate;
    }

    public void setRouteDate(LocalDate routeDate) {
        this.routeDate = routeDate;
    }

    public Integer getStartWarehouseId() {
        return startWarehouseId;
    }

    public void setStartWarehouseId(Integer startWarehouseId) {
        this.startWarehouseId = startWarehouseId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public BigDecimal getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(BigDecimal totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public BigDecimal getEstimatedDurationMin() {
        return estimatedDurationMin;
    }

    public void setEstimatedDurationMin(BigDecimal estimatedDurationMin) {
        this.estimatedDurationMin = estimatedDurationMin;
    }

    public BigDecimal getPredictedCost() {
        return predictedCost;
    }

    public void setPredictedCost(BigDecimal predictedCost) {
        this.predictedCost = predictedCost;
    }

    public BigDecimal getPredictedDelayRisk() {
        return predictedDelayRisk;
    }

    public void setPredictedDelayRisk(BigDecimal predictedDelayRisk) {
        this.predictedDelayRisk = predictedDelayRisk;
    }

    public BigDecimal getOptimizationScore() {
        return optimizationScore;
    }

    public void setOptimizationScore(BigDecimal optimizationScore) {
        this.optimizationScore = optimizationScore;
    }

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }
}