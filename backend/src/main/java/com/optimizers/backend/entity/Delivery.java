package com.optimizers.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Integer deliveryId;

    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "package_weight", precision = 10, scale = 2)
    private BigDecimal packageWeight;

    @Column(name = "package_volume", precision = 10, scale = 2)
    private BigDecimal packageVolume;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Column(name = "time_window_start")
    private LocalDateTime timeWindowStart;

    @Column(name = "time_window_end")
    private LocalDateTime timeWindowEnd;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Delivery() {
    }

    public Delivery(Integer deliveryId, Integer warehouseId, String customerName, String contactNumber,
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

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
        if (this.priority == null) {
            this.priority = "MEDIUM";
        }
    }
}