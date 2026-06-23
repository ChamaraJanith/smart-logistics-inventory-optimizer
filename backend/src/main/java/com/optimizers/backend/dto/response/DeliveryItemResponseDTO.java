package com.optimizers.backend.dto.response;

import java.math.BigDecimal;

public class DeliveryItemResponseDTO {
    private Integer deliveryItemId;
    private Integer deliveryId;
    private Integer itemId;
    private String itemName;
    private String sku;
    private BigDecimal quantity;
    private Integer allocatedStockId;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public DeliveryItemResponseDTO() {}

    public DeliveryItemResponseDTO(Integer deliveryItemId, Integer deliveryId, Integer itemId, String itemName,
                                   String sku, BigDecimal quantity, Integer allocatedStockId, BigDecimal unitPrice,
                                   BigDecimal totalPrice) {
        this.deliveryItemId = deliveryItemId;
        this.deliveryId = deliveryId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.sku = sku;
        this.quantity = quantity;
        this.allocatedStockId = allocatedStockId;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public Integer getDeliveryItemId() {
        return deliveryItemId;
    }

    public void setDeliveryItemId(Integer deliveryItemId) {
        this.deliveryItemId = deliveryItemId;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getAllocatedStockId() {
        return allocatedStockId;
    }

    public void setAllocatedStockId(Integer allocatedStockId) {
        this.allocatedStockId = allocatedStockId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
