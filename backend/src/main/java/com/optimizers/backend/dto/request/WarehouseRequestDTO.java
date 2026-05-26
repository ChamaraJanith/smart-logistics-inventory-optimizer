package com.optimizers.backend.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public class WarehouseRequestDTO {

    @NotBlank(message = "Warehouse name is required")
    private String name;

    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String managerName;
    private String contactNumber;
    private BigDecimal totalCapacitySqm;
    private String status;

    public WarehouseRequestDTO() {
    }

    public WarehouseRequestDTO(String name, String address, BigDecimal latitude, BigDecimal longitude,
                               String managerName, String contactNumber, BigDecimal totalCapacitySqm, String status) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managerName = managerName;
        this.contactNumber = contactNumber;
        this.totalCapacitySqm = totalCapacitySqm;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public BigDecimal getTotalCapacitySqm() {
        return totalCapacitySqm;
    }

    public void setTotalCapacitySqm(BigDecimal totalCapacitySqm) {
        this.totalCapacitySqm = totalCapacitySqm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}