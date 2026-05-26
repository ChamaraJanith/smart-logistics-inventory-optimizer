package com.optimizers.backend.dto.response;

import java.math.BigDecimal;

public class DeliveryLocationDTO {

    private Integer deliveryId;
    private String customerName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private String deliveryAddress;

    public DeliveryLocationDTO() {
    }

    public DeliveryLocationDTO(Integer deliveryId, String customerName,
                               BigDecimal latitude, BigDecimal longitude,
                               String status, String deliveryAddress) {
        this.deliveryId = deliveryId;
        this.customerName = customerName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
    }

    public Integer getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Integer deliveryId) { this.deliveryId = deliveryId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}