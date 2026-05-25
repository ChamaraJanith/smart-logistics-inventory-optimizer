package com.optimizers.backend.dto.response;

import java.time.LocalDateTime;

public class DriverResponseDTO {
    private Long driverId;
    private String driverName;
    private String phone;
    private String licenseNo;
    private String status;
    private Long vehicleId;
    private String vehicleNumber;
    private LocalDateTime createdAt;

    public DriverResponseDTO() {
    }

    public DriverResponseDTO(Long driverId, String driverName, String phone, String licenseNo,
                             String status, Long vehicleId, String vehicleNumber, LocalDateTime createdAt) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.phone = phone;
        this.licenseNo = licenseNo;
        this.status = status;
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.createdAt = createdAt;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}