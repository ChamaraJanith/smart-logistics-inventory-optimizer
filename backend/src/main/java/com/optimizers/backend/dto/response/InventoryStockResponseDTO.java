// dto/response/InventoryStockResponseDTO.java
package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryStockResponseDTO {

    private Integer stockId;
    private Integer itemId;
    private String itemName;
    private String sku;
    private Integer warehouseId;
    private String warehouseName;
    private BigDecimal quantityOnHand;
    private BigDecimal reservedQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal reorderLevel;
    private BigDecimal reorderQuantity;
    private BigDecimal maxStockLevel;
    private String stockStatus; // NORMAL, LOW, CRITICAL, OUT_OF_STOCK
    private LocalDateTime lastUpdated;

    public InventoryStockResponseDTO() {}

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

    public BigDecimal getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(BigDecimal v) { this.quantityOnHand = v; }

    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(BigDecimal v) { this.reservedQuantity = v; }

    public BigDecimal getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(BigDecimal v) { this.availableQuantity = v; }

    public BigDecimal getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(BigDecimal v) { this.reorderLevel = v; }

    public BigDecimal getReorderQuantity() { return reorderQuantity; }
    public void setReorderQuantity(BigDecimal v) { this.reorderQuantity = v; }

    public BigDecimal getMaxStockLevel() { return maxStockLevel; }
    public void setMaxStockLevel(BigDecimal v) { this.maxStockLevel = v; }

    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}