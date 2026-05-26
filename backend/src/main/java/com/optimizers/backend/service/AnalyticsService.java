package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.response.DriverPerformanceDTO;
import com.optimizers.backend.dto.response.TrendDataDTO;
import com.optimizers.backend.dto.response.VehicleUtilizationDTO;
import com.optimizers.backend.dto.response.DeliveryLocationDTO;

public interface AnalyticsService {
    List<TrendDataDTO> getDeliveryTrends(String period);
    List<TrendDataDTO> getRouteTrends(String period);
    List<DriverPerformanceDTO> getDriverPerformance();
    List<VehicleUtilizationDTO> getVehicleUtilization();
     List<DeliveryLocationDTO> getDeliveryHeatmap(String status);

}