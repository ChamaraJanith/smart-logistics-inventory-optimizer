// dto/response/ReorderAlertResponseDTO.java
package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReorderAlertResponseDTO {

    private Integer alertId;
    private Integer stockId;
    private Integer itemId;
    private String itemName;
    private String sku;
    private Integer warehouseId;
    private String warehouseName;
    private String alertType;
    private BigDecimal currentStock;
    private BigDecimal reorderLevel;
    private BigDecimal suggestedReorderQty;
    private String severity;
    private String status;
    private LocalDateTime triggeredAt;
    private LocalDateTime resolvedAt;

    public ReorderAlertResponseDTO() {}

    public Integer getAlertId() { return alertId; }
    public void setAlertId(Integer alertId) { this.alertId = alertId; }

    public Integer getStockId() { return stockId; }
    public void setStockId(Integer stockId) { this.stockId = stockId; }

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public BigDecimal getCurrentStock() { return currentStock; }
    public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }

    public BigDecimal getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(BigDecimal reorderLevel) { this.reorderLevel = reorderLevel; }

    public BigDecimal getSuggestedReorderQty() { return suggestedReorderQty; }
    public void setSuggestedReorderQty(BigDecimal suggestedReorderQty) { this.suggestedReorderQty = suggestedReorderQty; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}