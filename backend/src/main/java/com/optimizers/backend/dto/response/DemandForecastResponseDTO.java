// dto/response/DemandForecastResponseDTO.java
package com.optimizers.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DemandForecastResponseDTO {

    private Integer forecastId;
    private Integer itemId;
    private String itemName;
    private String sku;
    private Integer warehouseId;
    private String warehouseName;
    private LocalDate forecastDate;
    private BigDecimal predictedDemand;
    private BigDecimal confidenceScore;
    private String seasonPattern;
    private String modelVersion;
    private LocalDateTime createdAt;

    public DemandForecastResponseDTO() {}

    public Integer getForecastId() { return forecastId; }
    public void setForecastId(Integer forecastId) { this.forecastId = forecastId; }

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

    public LocalDate getForecastDate() { return forecastDate; }
    public void setForecastDate(LocalDate forecastDate) { this.forecastDate = forecastDate; }

    public BigDecimal getPredictedDemand() { return predictedDemand; }
    public void setPredictedDemand(BigDecimal predictedDemand) { this.predictedDemand = predictedDemand; }

    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getSeasonPattern() { return seasonPattern; }
    public void setSeasonPattern(String seasonPattern) { this.seasonPattern = seasonPattern; }

    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}