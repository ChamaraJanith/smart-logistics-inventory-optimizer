package com.optimizers.backend.dto.response;

import java.time.LocalDate;
import java.util.List;

public class RouteSummaryDTO {

    private Integer routeId;
    private Integer vehicleId;
    private Integer driverId;
    private LocalDate routeDate;
    private String startLocation;
    private String endLocation;
    private String routeStatus;
    private List<RouteDeliveryDetailDTO> deliveries;

    public RouteSummaryDTO() {
    }

    public RouteSummaryDTO(Integer routeId, Integer vehicleId, Integer driverId,
                           LocalDate routeDate, String startLocation,
                           String endLocation, String routeStatus,
                           List<RouteDeliveryDetailDTO> deliveries) {
        this.routeId = routeId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.routeDate = routeDate;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.routeStatus = routeStatus;
        this.deliveries = deliveries;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public LocalDate getRouteDate() {
        return routeDate;
    }

    public void setRouteDate(LocalDate routeDate) {
        this.routeDate = routeDate;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    public List<RouteDeliveryDetailDTO> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<RouteDeliveryDetailDTO> deliveries) {
        this.deliveries = deliveries;
    }
}