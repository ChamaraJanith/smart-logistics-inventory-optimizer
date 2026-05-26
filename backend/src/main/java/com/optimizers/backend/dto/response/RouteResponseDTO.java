package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RouteResponseDTO {
    private Integer routeId;
    private Integer vehicleId;
    private String vehicleNumber;
    private Integer driverId;
    private String driverName;
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
    private LocalDateTime createdAt;

    public RouteResponseDTO() {
    }

    public RouteResponseDTO(Integer routeId, Integer vehicleId, String vehicleNumber, Integer driverId,
                            String driverName, LocalDate routeDate, Integer startWarehouseId,
                            String startLocation, String endLocation, BigDecimal totalDistanceKm,
                            BigDecimal estimatedDurationMin, BigDecimal predictedCost,
                            BigDecimal predictedDelayRisk, BigDecimal optimizationScore,
                            String routeStatus, LocalDateTime createdAt) {
        this.routeId = routeId;
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.driverId = driverId;
        this.driverName = driverName;
        this.routeDate = routeDate;
        this.startWarehouseId = startWarehouseId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.totalDistanceKm = totalDistanceKm;
        this.estimatedDurationMin = estimatedDurationMin;
        this.predictedCost = predictedCost;
        this.predictedDelayRisk = predictedDelayRisk;
        this.optimizationScore = optimizationScore;
        this.routeStatus = routeStatus;
        this.createdAt = createdAt;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}