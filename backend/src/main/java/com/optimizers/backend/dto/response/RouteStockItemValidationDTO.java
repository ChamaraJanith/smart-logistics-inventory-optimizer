package com.optimizers.backend.dto.response;

import java.math.BigDecimal;

public class RouteStockItemValidationDTO {
    private Integer itemId;
    private String itemName;
    private String sku;
    private BigDecimal quantityRequired;
    private BigDecimal availableQuantity;
    private boolean isShort;

    public RouteStockItemValidationDTO() {}

    public RouteStockItemValidationDTO(Integer itemId, String itemName, String sku, BigDecimal quantityRequired,
                                       BigDecimal availableQuantity, boolean isShort) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.sku = sku;
        this.quantityRequired = quantityRequired;
        this.availableQuantity = availableQuantity;
        this.isShort = isShort;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(BigDecimal quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public boolean getIsShort() {
        return isShort;
    }

    public void setIsShort(boolean isShort) {
        this.isShort = isShort;
    }
}
