package com.optimizers.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class DriverRequestDTO {

    @NotBlank(message = "Driver name is required")
    private String driverName;

    private String phone;

    @NotBlank(message = "License number is required")
    private String licenseNo;

    private String status;
    private Integer vehicleId;

    public DriverRequestDTO() {
    }

    public DriverRequestDTO(String driverName, String phone, String licenseNo, String status, Integer vehicleId) {
        this.driverName = driverName;
        this.phone = phone;
        this.licenseNo = licenseNo;
        this.status = status;
        this.vehicleId = vehicleId;
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

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
}