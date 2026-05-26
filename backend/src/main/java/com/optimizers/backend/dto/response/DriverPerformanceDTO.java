package com.optimizers.backend.dto.response;

public class DriverPerformanceDTO {

    private Integer driverId;
    private String driverName;
    private long totalRoutes;
    private long completedRoutes;
    private long inProgressRoutes;
    private long plannedRoutes;

    public DriverPerformanceDTO() {
    }

    public DriverPerformanceDTO(Integer driverId, String driverName, long totalRoutes,
                                long completedRoutes, long inProgressRoutes, long plannedRoutes) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.totalRoutes = totalRoutes;
        this.completedRoutes = completedRoutes;
        this.inProgressRoutes = inProgressRoutes;
        this.plannedRoutes = plannedRoutes;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public long getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(long totalRoutes) {
        this.totalRoutes = totalRoutes;
    }

    public long getCompletedRoutes() {
        return completedRoutes;
    }

    public void setCompletedRoutes(long completedRoutes) {
        this.completedRoutes = completedRoutes;
    }

    public long getInProgressRoutes() {
        return inProgressRoutes;
    }

    public void setInProgressRoutes(long inProgressRoutes) {
        this.inProgressRoutes = inProgressRoutes;
    }

    public long getPlannedRoutes() {
        return plannedRoutes;
    }

    public void setPlannedRoutes(long plannedRoutes) {
        this.plannedRoutes = plannedRoutes;
    }
}