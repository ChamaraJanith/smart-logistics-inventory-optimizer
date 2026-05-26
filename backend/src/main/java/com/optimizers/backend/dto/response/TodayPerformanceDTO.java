package com.optimizers.backend.dto.response;

public class TodayPerformanceDTO {

    private long todayRoutes;
    private long completedRoutes;
    private long inProgressRoutes;
    private long todayDeliveries;
    private long deliveredDeliveries;
    private long pendingDeliveries;

    public TodayPerformanceDTO() {
    }

    public TodayPerformanceDTO(long todayRoutes, long completedRoutes, long inProgressRoutes,
                               long todayDeliveries, long deliveredDeliveries, long pendingDeliveries) {
        this.todayRoutes = todayRoutes;
        this.completedRoutes = completedRoutes;
        this.inProgressRoutes = inProgressRoutes;
        this.todayDeliveries = todayDeliveries;
        this.deliveredDeliveries = deliveredDeliveries;
        this.pendingDeliveries = pendingDeliveries;
    }

    public long getTodayRoutes() { return todayRoutes; }
    public void setTodayRoutes(long todayRoutes) { this.todayRoutes = todayRoutes; }

    public long getCompletedRoutes() { return completedRoutes; }
    public void setCompletedRoutes(long completedRoutes) { this.completedRoutes = completedRoutes; }

    public long getInProgressRoutes() { return inProgressRoutes; }
    public void setInProgressRoutes(long inProgressRoutes) { this.inProgressRoutes = inProgressRoutes; }

    public long getTodayDeliveries() { return todayDeliveries; }
    public void setTodayDeliveries(long todayDeliveries) { this.todayDeliveries = todayDeliveries; }

    public long getDeliveredDeliveries() { return deliveredDeliveries; }
    public void setDeliveredDeliveries(long deliveredDeliveries) { this.deliveredDeliveries = deliveredDeliveries; }

    public long getPendingDeliveries() { return pendingDeliveries; }
    public void setPendingDeliveries(long pendingDeliveries) { this.pendingDeliveries = pendingDeliveries; }
}