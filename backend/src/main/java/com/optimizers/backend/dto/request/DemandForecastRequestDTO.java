// dto/request/DemandForecastRequestDTO.java
package com.optimizers.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public class DemandForecastRequestDTO {

    @NotNull(message = "Item ID is required")
    private Integer itemId;

    @NotNull(message = "Warehouse ID is required")
    private Integer warehouseId;

    // How many days to predict (default 7)
    private Integer forecastDays = 7;

    public DemandForecastRequestDTO() {}

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public Integer getForecastDays() { return forecastDays; }
    public void setForecastDays(Integer forecastDays) { this.forecastDays = forecastDays; }
}