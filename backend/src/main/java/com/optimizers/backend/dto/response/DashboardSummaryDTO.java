package com.optimizers.backend.dto.response;

public class DashboardSummaryDTO {

    private long totalDeliveries;
    private long pendingDeliveries;
    private long assignedDeliveries;
    private long inTransitDeliveries;
    private long deliveredDeliveries;

    private long totalRoutes;
    private long plannedRoutes;
    private long inProgressRoutes;
    private long completedRoutes;

    private long todayRoutes;
    private long todayDeliveries;

    public DashboardSummaryDTO() {
    }

    public DashboardSummaryDTO(long totalDeliveries, long pendingDeliveries, long assignedDeliveries,
                               long inTransitDeliveries, long deliveredDeliveries,
                               long totalRoutes, long plannedRoutes, long inProgressRoutes,
                               long completedRoutes, long todayRoutes, long todayDeliveries) {
        this.totalDeliveries = totalDeliveries;
        this.pendingDeliveries = pendingDeliveries;
        this.assignedDeliveries = assignedDeliveries;
        this.inTransitDeliveries = inTransitDeliveries;
        this.deliveredDeliveries = deliveredDeliveries;
        this.totalRoutes = totalRoutes;
        this.plannedRoutes = plannedRoutes;
        this.inProgressRoutes = inProgressRoutes;
        this.completedRoutes = completedRoutes;
        this.todayRoutes = todayRoutes;
        this.todayDeliveries = todayDeliveries;
    }

    public long getTotalDeliveries() {
        return totalDeliveries;
    }

    public void setTotalDeliveries(long totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }

    public long getPendingDeliveries() {
        return pendingDeliveries;
    }

    public void setPendingDeliveries(long pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }

    public long getAssignedDeliveries() {
        return assignedDeliveries;
    }

    public void setAssignedDeliveries(long assignedDeliveries) {
        this.assignedDeliveries = assignedDeliveries;
    }

    public long getInTransitDeliveries() {
        return inTransitDeliveries;
    }

    public void setInTransitDeliveries(long inTransitDeliveries) {
        this.inTransitDeliveries = inTransitDeliveries;
    }

    public long getDeliveredDeliveries() {
        return deliveredDeliveries;
    }

    public void setDeliveredDeliveries(long deliveredDeliveries) {
        this.deliveredDeliveries = deliveredDeliveries;
    }

    public long getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(long totalRoutes) {
        this.totalRoutes = totalRoutes;
    }

    public long getPlannedRoutes() {
        return plannedRoutes;
    }

    public void setPlannedRoutes(long plannedRoutes) {
        this.plannedRoutes = plannedRoutes;
    }

    public long getInProgressRoutes() {
        return inProgressRoutes;
    }

    public void setInProgressRoutes(long inProgressRoutes) {
        this.inProgressRoutes = inProgressRoutes;
    }

    public long getCompletedRoutes() {
        return completedRoutes;
    }

    public void setCompletedRoutes(long completedRoutes) {
        this.completedRoutes = completedRoutes;
    }

    public long getTodayRoutes() {
        return todayRoutes;
    }

    public void setTodayRoutes(long todayRoutes) {
        this.todayRoutes = todayRoutes;
    }

    public long getTodayDeliveries() {
        return todayDeliveries;
    }

    public void setTodayDeliveries(long todayDeliveries) {
        this.todayDeliveries = todayDeliveries;
    }
}