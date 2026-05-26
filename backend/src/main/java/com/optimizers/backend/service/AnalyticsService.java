package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.response.TrendDataDTO;

public interface AnalyticsService {
    List<TrendDataDTO> getDeliveryTrends(String period);
    List<TrendDataDTO> getRouteTrends(String period);
}