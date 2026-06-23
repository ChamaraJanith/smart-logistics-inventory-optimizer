package com.optimizers.backend.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "delivery_item")
public class DeliveryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_item_id")
    private Integer deliveryItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocated_stock_id")
    private InventoryStock allocatedStock;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_price", insertable = false, updatable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    public DeliveryItem() {}

    public DeliveryItem(Integer deliveryItemId, Delivery delivery, InventoryItem item, BigDecimal quantity,
                        InventoryStock allocatedStock, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.deliveryItemId = deliveryItemId;
        this.delivery = delivery;
        this.item = item;
        this.quantity = quantity;
        this.allocatedStock = allocatedStock;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public Integer getDeliveryItemId() {
        return deliveryItemId;
    }

    public void setDeliveryItemId(Integer deliveryItemId) {
        this.deliveryItemId = deliveryItemId;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public InventoryStock getAllocatedStock() {
        return allocatedStock;
    }

    public void setAllocatedStock(InventoryStock allocatedStock) {
        this.allocatedStock = allocatedStock;
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
