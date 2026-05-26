// service/DemandForecastService.java
package com.optimizers.backend.service;

import java.util.List;
import com.optimizers.backend.dto.request.DemandForecastRequestDTO;
import com.optimizers.backend.dto.response.DemandForecastResponseDTO;

public interface DemandForecastService {
    // Single item forecast generate
    List<DemandForecastResponseDTO> generateForecast(
            DemandForecastRequestDTO requestDTO);

    // Warehouse එකේ සියලුම items forecast
    List<DemandForecastResponseDTO> generateForecastForWarehouse(
            Integer warehouseId, Integer forecastDays);

    // Existing forecasts retrieve 
    List<DemandForecastResponseDTO> getForecastByItem(Integer itemId);

    List<DemandForecastResponseDTO> getForecastByWarehouse(Integer warehouseId);

    // Next 7 days total predicted demand for an item
    java.math.BigDecimal getPredictedDemand7d(
            Integer itemId, Integer warehouseId);
}