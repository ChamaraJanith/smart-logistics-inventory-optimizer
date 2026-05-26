package com.optimizers.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "route_date", nullable = false)
    private LocalDate routeDate;

    @Column(name = "start_warehouse_id")
    private Integer startWarehouseId;

    @Column(name = "start_location", length = 150)
    private String startLocation;

    @Column(name = "end_location", length = 150)
    private String endLocation;

    @Column(name = "total_distance_km", precision = 10, scale = 2)
    private BigDecimal totalDistanceKm;

    @Column(name = "estimated_duration_min", precision = 10, scale = 2)
    private BigDecimal estimatedDurationMin;

    @Column(name = "predicted_cost", precision = 10, scale = 2)
    private BigDecimal predictedCost;

    @Column(name = "predicted_delay_risk", precision = 5, scale = 2)
    private BigDecimal predictedDelayRisk;

    @Column(name = "optimization_score", precision = 5, scale = 2)
    private BigDecimal optimizationScore;

    @Column(name = "route_status", length = 30)
    private String routeStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Route() {
    }

    public Route(Integer routeId, Vehicle vehicle, Driver driver, LocalDate routeDate,
                 Integer startWarehouseId, String startLocation, String endLocation,
                 BigDecimal totalDistanceKm, BigDecimal estimatedDurationMin,
                 BigDecimal predictedCost, BigDecimal predictedDelayRisk,
                 BigDecimal optimizationScore, String routeStatus, LocalDateTime createdAt) {
        this.routeId = routeId;
        this.vehicle = vehicle;
        this.driver = driver;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.routeStatus == null) {
            this.routeStatus = "PLANNED";
        }
    }
}