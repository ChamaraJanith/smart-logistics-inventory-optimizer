package com.optimizers.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecentRouteDTO {

    private Integer routeId;
    private String vehicleNumber;
    private String driverName;
    private String startLocation;
    private String endLocation;
    private String routeStatus;
    private LocalDate routeDate;
    private Double totalDistanceKm;
    private LocalDateTime createdAt;

    public RecentRouteDTO() {
    }

    public RecentRouteDTO(Integer routeId, String vehicleNumber, String driverName,
                          String startLocation, String endLocation, String routeStatus,
                          LocalDate routeDate, Double totalDistanceKm, LocalDateTime createdAt) {
        this.routeId = routeId;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.routeStatus = routeStatus;
        this.routeDate = routeDate;
        this.totalDistanceKm = totalDistanceKm;
        this.createdAt = createdAt;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    public LocalDate getRouteDate() {
        return routeDate;
    }

    public void setRouteDate(LocalDate routeDate) {
        this.routeDate = routeDate;
    }

    public Double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(Double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}