package com.optimizers.backend.dto.request;

import java.math.BigDecimal;

public class VehicleRequestDTO {
    private String vehicleNumber;
    private String vehicleType;
    private BigDecimal capacityKg;
    private BigDecimal maxVolume;
    private String fuelType;
    private String currentStatus;
    private BigDecimal currentLatitude;
    private BigDecimal currentLongitude;

    public VehicleRequestDTO() {
    }

    public VehicleRequestDTO(String vehicleNumber, String vehicleType, BigDecimal capacityKg,
                             BigDecimal maxVolume, String fuelType, String currentStatus,
                             BigDecimal currentLatitude, BigDecimal currentLongitude) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.capacityKg = capacityKg;
        this.maxVolume = maxVolume;
        this.fuelType = fuelType;
        this.currentStatus = currentStatus;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
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
}