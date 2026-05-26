// dto/response/InventoryItemResponseDTO.java
package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryItemResponseDTO {

    private Integer itemId;
    private Integer warehouseId;
    private String warehouseName;
    private String sku;
    private String itemName;
    private String category;
    private String unit;
    private BigDecimal unitWeightKg;
    private BigDecimal unitVolume;
    private BigDecimal unitPrice;
    private String status;
    private LocalDateTime createdAt;

    public InventoryItemResponseDTO() {}

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getUnitWeightKg() { return unitWeightKg; }
    public void setUnitWeightKg(BigDecimal v) { this.unitWeightKg = v; }

    public BigDecimal getUnitVolume() { return unitVolume; }
    public void setUnitVolume(BigDecimal v) { this.unitVolume = v; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal v) { this.unitPrice = v; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}