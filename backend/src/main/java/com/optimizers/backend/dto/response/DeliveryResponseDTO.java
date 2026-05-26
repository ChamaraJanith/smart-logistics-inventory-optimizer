package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DeliveryResponseDTO {
    private Integer deliveryId;
    private Integer warehouseId;
    private String customerName;
    private String contactNumber;
    private String deliveryAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal packageWeight;
    private BigDecimal packageVolume;
    private String priority;
    private String status;
    private LocalDateTime requestedDate;
    private LocalDateTime timeWindowStart;
    private LocalDateTime timeWindowEnd;
    private LocalDateTime createdAt;

    public DeliveryResponseDTO() {
    }

    public DeliveryResponseDTO(Integer deliveryId, Integer warehouseId, String customerName, String contactNumber,
                               String deliveryAddress, BigDecimal latitude, BigDecimal longitude,
                               BigDecimal packageWeight, BigDecimal packageVolume, String priority,
                               String status, LocalDateTime requestedDate, LocalDateTime timeWindowStart,
                               LocalDateTime timeWindowEnd, LocalDateTime createdAt) {
        this.deliveryId = deliveryId;
        this.warehouseId = warehouseId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.deliveryAddress = deliveryAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.packageWeight = packageWeight;
        this.packageVolume = packageVolume;
        this.priority = priority;
        this.status = status;
        this.requestedDate = requestedDate;
        this.timeWindowStart = timeWindowStart;
        this.timeWindowEnd = timeWindowEnd;
        this.createdAt = createdAt;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public BigDecimal getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(BigDecimal packageWeight) {
        this.packageWeight = packageWeight;
    }

    public BigDecimal getPackageVolume() {
        return packageVolume;
    }

    public void setPackageVolume(BigDecimal packageVolume) {
        this.packageVolume = packageVolume;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDateTime getTimeWindowStart() {
        return timeWindowStart;
    }

    public void setTimeWindowStart(LocalDateTime timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }

    public LocalDateTime getTimeWindowEnd() {
        return timeWindowEnd;
    }

    public void setTimeWindowEnd(LocalDateTime timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}