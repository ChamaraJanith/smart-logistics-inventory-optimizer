package com.optimizers.backend.dto.response;

import java.time.LocalDateTime;

public class RouteDeliveryResponseDTO {

    private Integer routeDeliveryId;
    private Integer routeId;
    private Integer deliveryId;
    private Integer stopSequence;
    private LocalDateTime predictedEta;
    private LocalDateTime estimatedArrivalTime;
    private LocalDateTime actualArrivalTime;
    private String stopStatus;
    private String customerName;
    private String deliveryAddress;

    public RouteDeliveryResponseDTO() {
    }

    public RouteDeliveryResponseDTO(Integer routeDeliveryId, Integer routeId, Integer deliveryId,
                                    Integer stopSequence, LocalDateTime predictedEta,
                                    LocalDateTime estimatedArrivalTime, LocalDateTime actualArrivalTime,
                                    String stopStatus, String customerName, String deliveryAddress) {
        this.routeDeliveryId = routeDeliveryId;
        this.routeId = routeId;
        this.deliveryId = deliveryId;
        this.stopSequence = stopSequence;
        this.predictedEta = predictedEta;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.actualArrivalTime = actualArrivalTime;
        this.stopStatus = stopStatus;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
    }

    public Integer getRouteDeliveryId() {
        return routeDeliveryId;
    }

    public void setRouteDeliveryId(Integer routeDeliveryId) {
        this.routeDeliveryId = routeDeliveryId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
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
}