package com.optimizers.backend.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public class DeliveryItemRequestDTO {

    @NotNull(message = "Item ID is required")
    private Integer itemId;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity;

    private BigDecimal unitPrice;

    public DeliveryItemRequestDTO() {}

    public DeliveryItemRequestDTO(Integer itemId, BigDecimal quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
