// dto/response/InventoryDashboardSummaryDTO.java
package com.optimizers.backend.dto.response;

public class InventoryDashboardSummaryDTO {

    // Total active warehouses count
    private long totalWarehouses;

    // Total active inventory items count
    private long totalItems;

    // Items where available_quantity <= reorder_level
    private long lowStockCount;

    // Items where available_quantity <= 0
    private long outOfStockCount;

    // Open reorder alerts count
    private long openAlertsCount;

    // Critical severity alerts count
    private long criticalAlertsCount;

    // Open anomalies count
    private long openAnomaliesCount;

    // Total stock transactions today
    private long todayTransactionsCount;

    public InventoryDashboardSummaryDTO() {}

    public InventoryDashboardSummaryDTO(
            long totalWarehouses,
            long totalItems,
            long lowStockCount,
            long outOfStockCount,
            long openAlertsCount,
            long criticalAlertsCount,
            long openAnomaliesCount,
            long todayTransactionsCount) {
        this.totalWarehouses = totalWarehouses;
        this.totalItems = totalItems;
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
        this.openAlertsCount = openAlertsCount;
        this.criticalAlertsCount = criticalAlertsCount;
        this.openAnomaliesCount = openAnomaliesCount;
        this.todayTransactionsCount = todayTransactionsCount;
    }

    public long getTotalWarehouses() { return totalWarehouses; }
    public void setTotalWarehouses(long totalWarehouses) { this.totalWarehouses = totalWarehouses; }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }

    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }

    public long getOutOfStockCount() { return outOfStockCount; }
    public void setOutOfStockCount(long outOfStockCount) { this.outOfStockCount = outOfStockCount; }

    public long getOpenAlertsCount() { return openAlertsCount; }
    public void setOpenAlertsCount(long openAlertsCount) { this.openAlertsCount = openAlertsCount; }

    public long getCriticalAlertsCount() { return criticalAlertsCount; }
    public void setCriticalAlertsCount(long criticalAlertsCount) { this.criticalAlertsCount = criticalAlertsCount; }

    public long getOpenAnomaliesCount() { return openAnomaliesCount; }
    public void setOpenAnomaliesCount(long openAnomaliesCount) { this.openAnomaliesCount = openAnomaliesCount; }

    public long getTodayTransactionsCount() { return todayTransactionsCount; }
    public void setTodayTransactionsCount(long todayTransactionsCount) { this.todayTransactionsCount = todayTransactionsCount; }
}