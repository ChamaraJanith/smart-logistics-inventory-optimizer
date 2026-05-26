package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.response.DashboardSummaryDTO;
import com.optimizers.backend.dto.response.RecentDeliveryDTO;
import com.optimizers.backend.dto.response.RecentRouteDTO;
import com.optimizers.backend.dto.response.StatusCountDTO;
import com.optimizers.backend.dto.response.TodayPerformanceDTO;

public interface DashboardService {

    DashboardSummaryDTO getDashboardSummary();

    List<StatusCountDTO> getDeliveryStatusCounts();

    List<StatusCountDTO> getRouteStatusCounts();

    List<RecentRouteDTO> getRecentRoutes();

    List<RecentDeliveryDTO> getRecentDeliveries();

    TodayPerformanceDTO getTodayPerformance();
}