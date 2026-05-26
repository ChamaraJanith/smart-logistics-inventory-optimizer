package com.optimizers.backend.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class RouteDeliveryRequestDTO {

    @NotNull(message = "Route ID is required")
    private Integer routeId;

    @NotNull(message = "Delivery ID is required")
    private Integer deliveryId;

    @NotNull(message = "Stop sequence is required")
    private Integer stopSequence;

    private LocalDateTime predictedEta;
    private LocalDateTime estimatedArrivalTime;
    private LocalDateTime actualArrivalTime;
    private String stopStatus;

    public RouteDeliveryRequestDTO() {
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
}