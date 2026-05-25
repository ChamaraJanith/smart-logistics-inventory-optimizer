package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VehicleResponseDTO {
    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private BigDecimal capacityKg;
    private BigDecimal maxVolume;
    private String fuelType;
    private String currentStatus;
    private BigDecimal currentLatitude;
    private BigDecimal currentLongitude;
    private LocalDateTime createdAt;

    public VehicleResponseDTO() {
    }

    public VehicleResponseDTO(Long vehicleId, String vehicleNumber, String vehicleType,
                              BigDecimal capacityKg, BigDecimal maxVolume, String fuelType,
                              String currentStatus, BigDecimal currentLatitude,
                              BigDecimal currentLongitude, LocalDateTime createdAt) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.capacityKg = capacityKg;
        this.maxVolume = maxVolume;
        this.fuelType = fuelType;
        this.currentStatus = currentStatus;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.createdAt = createdAt;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public BigDecimal getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(BigDecimal capacityKg) {
        this.capacityKg = capacityKg;
    }

    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume) {
        this.maxVolume = maxVolume;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public BigDecimal getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(BigDecimal currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public BigDecimal getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(BigDecimal currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}