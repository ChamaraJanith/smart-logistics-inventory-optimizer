package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RouteDeliveryDetailDTO {

    private Integer routeDeliveryId;
    private Integer deliveryId;
    private Integer stopSequence;
    private String customerName;
    private String contactNumber;
    private String deliveryAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal packageWeight;
    private BigDecimal packageVolume;
    private String priority;
    private LocalDateTime predictedEta;
    private LocalDateTime estimatedArrivalTime;
    private LocalDateTime actualArrivalTime;
    private String stopStatus;

    public RouteDeliveryDetailDTO() {
    }

    public RouteDeliveryDetailDTO(Integer routeDeliveryId, Integer deliveryId,
                                  Integer stopSequence, String customerName,
                                  String contactNumber, String deliveryAddress,
                                  BigDecimal latitude, BigDecimal longitude,
                                  BigDecimal packageWeight, BigDecimal packageVolume,
                                  String priority, LocalDateTime predictedEta,
                                  LocalDateTime estimatedArrivalTime,
                                  LocalDateTime actualArrivalTime, String stopStatus) {
        this.routeDeliveryId = routeDeliveryId;
        this.deliveryId = deliveryId;
        this.stopSequence = stopSequence;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.deliveryAddress = deliveryAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.packageWeight = packageWeight;
        this.packageVolume = packageVolume;
        this.priority = priority;
        this.predictedEta = predictedEta;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.actualArrivalTime = actualArrivalTime;
        this.stopStatus = stopStatus;
    }

    public Integer getRouteDeliveryId() {
        return routeDeliveryId;
    }

    public void setRouteDeliveryId(Integer routeDeliveryId) {
        this.routeDeliveryId = routeDeliveryId;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(Integer stopSequence) {
        this.stopSequence = stopSequence;
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

    public LocalDateTime getPredictedEta() {
        return predictedEta;
    }

    public void setPredictedEta(LocalDateTime predictedEta) {
        this.predictedEta = predictedEta;
    }

    public LocalDateTime getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(LocalDateTime estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public String getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }
}