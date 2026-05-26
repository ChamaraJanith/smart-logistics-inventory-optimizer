package com.optimizers.backend.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class RouteDeliveryStatusUpdateDTO {

    @NotBlank(message = "Stop status is required")
    private String stopStatus;

    private LocalDateTime actualArrivalTime;

    public RouteDeliveryStatusUpdateDTO() {
    }

    public String getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }
}