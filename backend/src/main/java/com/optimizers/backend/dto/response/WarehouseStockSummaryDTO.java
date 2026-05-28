// dto/response/WarehouseStockSummaryDTO.java
package com.optimizers.backend.dto.response;

public class WarehouseStockSummaryDTO {

    // Warehouse details
    private Integer warehouseId;
    private String warehouseName;
    private String warehouseStatus;

    // Stock counts for this warehouse
    private long totalItems;
    private long normalStockCount;
    private long lowStockCount;
    private long criticalStockCount;
    private long outOfStockCount;

    // Open alerts for this warehouse
    private long openAlertsCount;

    public WarehouseStockSummaryDTO() {}

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getWarehouseStatus() { return warehouseStatus; }
    public void setWarehouseStatus(String warehouseStatus) { this.warehouseStatus = warehouseStatus; }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }

    public long getNormalStockCount() { return normalStockCount; }
    public void setNormalStockCount(long normalStockCount) { this.normalStockCount = normalStockCount; }

    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }

    public long getCriticalStockCount() { return criticalStockCount; }
    public void setCriticalStockCount(long criticalStockCount) { this.criticalStockCount = criticalStockCount; }

    public long getOutOfStockCount() { return outOfStockCount; }
    public void setOutOfStockCount(long outOfStockCount) { this.outOfStockCount = outOfStockCount; }

    public long getOpenAlertsCount() { return openAlertsCount; }
    public void setOpenAlertsCount(long openAlertsCount) { this.openAlertsCount = openAlertsCount; }
}