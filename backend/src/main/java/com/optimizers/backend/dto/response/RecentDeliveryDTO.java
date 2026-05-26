package com.optimizers.backend.dto.response;

import java.time.LocalDateTime;

public class RecentDeliveryDTO {

    private Integer deliveryId;
    private String customerName;
    private String deliveryAddress;
    private String status;
    private LocalDateTime requestedDate;
    private LocalDateTime createdAt;

    public RecentDeliveryDTO() {
    }

    public RecentDeliveryDTO(Integer deliveryId, String customerName, String deliveryAddress,
                             String status, LocalDateTime requestedDate, LocalDateTime createdAt) {
        this.deliveryId = deliveryId;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.requestedDate = requestedDate;
        this.createdAt = createdAt;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}