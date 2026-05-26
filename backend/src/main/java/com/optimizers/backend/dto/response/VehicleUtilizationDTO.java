package com.optimizers.backend.dto.response;

import java.math.BigDecimal;

public class VehicleUtilizationDTO {

    private Integer vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private long totalRoutes;
    private long completedRoutes;
    private long inProgressRoutes;
    private long plannedRoutes;
    private BigDecimal totalDistanceKm;

    public VehicleUtilizationDTO() {
    }

    public VehicleUtilizationDTO(Integer vehicleId, String vehicleNumber, String vehicleType,
                                  long totalRoutes, long completedRoutes, long inProgressRoutes,
                                  long plannedRoutes, BigDecimal totalDistanceKm) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.totalRoutes = totalRoutes;
        this.completedRoutes = completedRoutes;
        this.inProgressRoutes = inProgressRoutes;
        this.plannedRoutes = plannedRoutes;
        this.totalDistanceKm = totalDistanceKm;
    }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public long getTotalRoutes() { return totalRoutes; }
    public void setTotalRoutes(long totalRoutes) { this.totalRoutes = totalRoutes; }

    public long getCompletedRoutes() { return completedRoutes; }
    public void setCompletedRoutes(long completedRoutes) { this.completedRoutes = completedRoutes; }

    public long getInProgressRoutes() { return inProgressRoutes; }
    public void setInProgressRoutes(long inProgressRoutes) { this.inProgressRoutes = inProgressRoutes; }

    public long getPlannedRoutes() { return plannedRoutes; }
    public void setPlannedRoutes(long plannedRoutes) { this.plannedRoutes = plannedRoutes; }

    public BigDecimal getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(BigDecimal totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }
}