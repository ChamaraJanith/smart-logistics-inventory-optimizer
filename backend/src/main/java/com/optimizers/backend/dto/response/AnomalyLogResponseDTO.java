// dto/response/AnomalyLogResponseDTO.java
package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AnomalyLogResponseDTO {

    private Integer anomalyId;
    private Integer itemId;
    private String itemName;
    private String sku;
    private Integer warehouseId;
    private String warehouseName;
    private String anomalyType;
    private String description;
    private BigDecimal deviationScore;
    private String status;
    private LocalDateTime detectedAt;
    private LocalDateTime resolvedAt;

    public AnomalyLogResponseDTO() {}

    public Integer getAnomalyId() { return anomalyId; }
    public void setAnomalyId(Integer anomalyId) { this.anomalyId = anomalyId; }

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