// dto/request/InventoryStockRequestDTO.java
package com.optimizers.backend.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class InventoryStockRequestDTO {

    @NotNull(message = "Item ID is required")
    private Integer itemId;

    @NotNull(message = "Warehouse ID is required")
    private Integer warehouseId;

    @NotNull(message = "Quantity on hand is required")
    private BigDecimal quantityOnHand;

    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @NotNull(message = "Reorder level is required")
    private BigDecimal reorderLevel;

    @NotNull(message = "Reorder quantity is required")
    private BigDecimal reorderQuantity;

    private BigDecimal maxStockLevel;

    public InventoryStockRequestDTO() {}

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public BigDecimal getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(BigDecimal quantityOnHand) { this.quantityOnHand = quantityOnHand; }

    public BigDecimal getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(BigDecimal reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public BigDecimal getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(BigDecimal reorderLevel) { this.reorderLevel = reorderLevel; }

    public BigDecimal getReorderQuantity() { return reorderQuantity; }
    public void setReorderQuantity(BigDecimal reorderQuantity) { this.reorderQuantity = reorderQuantity; }

    public BigDecimal getMaxStockLevel() { return maxStockLevel; }
    public void setMaxStockLevel(BigDecimal maxStockLevel) { this.maxStockLevel = maxStockLevel; }
}