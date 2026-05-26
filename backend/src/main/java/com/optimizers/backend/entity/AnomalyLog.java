// entity/AnomalyLog.java
package com.optimizers.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Entity class representing a detected anomaly in stock or delivery behavior.
 * Each record stores what was detected, when, and its current resolution status.
 */
@Entity
@Table(name = "anomaly_log")
public class AnomalyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anomaly_id")
    private Integer anomalyId;

    // The inventory item associated with this anomaly
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    // The warehouse where the anomaly was detected
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    // Type of anomaly: e.g. LARGE_DISPATCH, RAPID_DEPLETION, CRITICAL_STOCK_LEVEL
    @Column(name = "anomaly_type", nullable = false, length = 100)
    private String anomalyType;

    // Human-readable explanation of what triggered this anomaly
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // How far the value deviated from the expected normal (higher = more unusual)
    @Column(name = "deviation_score", precision = 8, scale = 4)
    private BigDecimal deviationScore;

    // Current status of the anomaly: OPEN or RESOLVED
    @Column(name = "status", length = 30)
    private String status = "OPEN";

    // Timestamp when the anomaly was first detected
    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    // Timestamp when the anomaly was resolved (null if still open)
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public AnomalyLog() {}

    /**
     * Automatically sets default values before the record is persisted to the database.
     */
    @PrePersist
    public void prePersist() {
        if (this.detectedAt == null) this.detectedAt = LocalDateTime.now();
        if (this.status == null) this.status = "OPEN";
    }

    // ── Getters and Setters ──────────────────────────────────────────

    public Integer getAnomalyId() { return anomalyId; }
    public void setAnomalyId(Integer anomalyId) { this.anomalyId = anomalyId; }

    public InventoryItem getItem() { return item; }
    public void setItem(InventoryItem item) { this.item = item; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    public String getAnomalyType() { return anomalyType; }
    public void setAnomalyType(String anomalyType) { this.anomalyType = anomalyType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getDeviationScore() { return deviationScore; }
    public void setDeviationScore(BigDecimal deviationScore) { this.deviationScore = deviationScore; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}