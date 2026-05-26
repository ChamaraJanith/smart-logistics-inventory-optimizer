package com.optimizers.backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.response.TrendDataDTO;
import com.optimizers.backend.repository.DeliveryRepository;
import com.optimizers.backend.repository.RouteRepository;
import com.optimizers.backend.service.AnalyticsService;
import com.optimizers.backend.dto.response.DriverPerformanceDTO;
import com.optimizers.backend.dto.response.VehicleUtilizationDTO;
import com.optimizers.backend.dto.response.DeliveryLocationDTO;
import java.math.BigDecimal;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public List<TrendDataDTO> getDeliveryTrends(String period) {
        if ("weekly".equalsIgnoreCase(period)) {
            return getWeeklyDeliveryTrends();
        } else if ("monthly".equalsIgnoreCase(period)) {
            return getMonthlyDeliveryTrends();
        }
        return new ArrayList<>();
    }

    @Override
    public List<TrendDataDTO> getRouteTrends(String period) {
        if ("weekly".equalsIgnoreCase(period)) {
            return getWeeklyRouteTrends();
        } else if ("monthly".equalsIgnoreCase(period)) {
            return getMonthlyRouteTrends();
        }
        return new ArrayList<>();
    }
    @Override
public List<DeliveryLocationDTO> getDeliveryHeatmap(String status) {
    List<Object[]> results;

    if (status != null && !status.isBlank()) {
        results = deliveryRepository.findDeliveryLocationsByStatus(status.toUpperCase());
    } else {
        results = deliveryRepository.findAllDeliveryLocations();
    }

    List<DeliveryLocationDTO> locations = new ArrayList<>();

    for (Object[] result : results) {
        Number deliveryId = (Number) result[0];
        String customerName = (String) result[1];
        BigDecimal latitude = result[2] != null
                ? new BigDecimal(result[2].toString()) : null;
        BigDecimal longitude = result[3] != null
                ? new BigDecimal(result[3].toString()) : null;
        String deliveryStatus = (String) result[4];
        String deliveryAddress = (String) result[5];

        locations.add(new DeliveryLocationDTO(
                deliveryId.intValue(),
                customerName,
                latitude,
                longitude,
                deliveryStatus,
                deliveryAddress
        ));
    }

    return locations;
}

    private List<TrendDataDTO> getWeeklyDeliveryTrends() {
        LocalDateTime startDate = LocalDateTime.now().minusWeeks(8);
        List<Object[]> results = deliveryRepository.findWeeklyDeliveryCounts(startDate);
        List<TrendDataDTO> trends = new ArrayList<>();

        for (Object[] result : results) {
            Number year = (Number) result[0];
            Number week = (Number) result[1];
            Number count = (Number) result[2];

            trends.add(new TrendDataDTO(
                    year.intValue() + "-W" + String.format("%02d", week.intValue()),
                    count.longValue()
            ));
        }

        return trends;
    }

    private List<TrendDataDTO> getMonthlyDeliveryTrends() {
        LocalDateTime startDate = LocalDateTime.now().minusMonths(6);
        List<Object[]> results = deliveryRepository.findMonthlyDeliveryCounts(startDate);
        List<TrendDataDTO> trends = new ArrayList<>();

        for (Object[] result : results) {
            Number year = (Number) result[0];
            Number month = (Number) result[1];
            Number count = (Number) result[2];

            trends.add(new TrendDataDTO(
                    year.intValue() + "-" + String.format("%02d", month.intValue()),
                    count.longValue()
            ));
        }

        return trends;
    }

    private List<TrendDataDTO> getWeeklyRouteTrends() {
        LocalDateTime startDate = LocalDateTime.now().minusWeeks(8);
        List<Object[]> results = routeRepository.findWeeklyRouteCounts(startDate);
        List<TrendDataDTO> trends = new ArrayList<>();

        for (Object[] result : results) {
            Number year = (Number) result[0];
            Number week = (Number) result[1];
            Number count = (Number) result[2];

            trends.add(new TrendDataDTO(
                    year.intValue() + "-W" + String.format("%02d", week.intValue()),
                    count.longValue()
            ));
        }

        return trends;
    }
    @Override
public List<VehicleUtilizationDTO> getVehicleUtilization() {
    List<Object[]> results = routeRepository.findVehicleUtilizationMetrics();
    List<VehicleUtilizationDTO> metrics = new ArrayList<>();

    for (Object[] result : results) {
        Number vehicleId = (Number) result[0];
        String vehicleNumber = (String) result[1];
        String vehicleType = (String) result[2];
        Number totalRoutes = (Number) result[3];
        Number completedRoutes = (Number) result[4];
        Number inProgressRoutes = (Number) result[5];
        Number plannedRoutes = (Number) result[6];
        BigDecimal totalDistanceKm = result[7] != null
                ? new BigDecimal(result[7].toString())
                : BigDecimal.ZERO;

        metrics.add(new VehicleUtilizationDTO(
                vehicleId.intValue(),
                vehicleNumber,
                vehicleType,
                totalRoutes.longValue(),
                completedRoutes.longValue(),
                inProgressRoutes.longValue(),
                plannedRoutes.longValue(),
                totalDistanceKm
        ));
    }

    return metrics;
}

    private List<TrendDataDTO> getMonthlyRouteTrends() {
        LocalDateTime startDate = LocalDateTime.now().minusMonths(6);
        List<Object[]> results = routeRepository.findMonthlyRouteCounts(startDate);
        List<TrendDataDTO> trends = new ArrayList<>();

        for (Object[] result : results) {
            Number year = (Number) result[0];
            Number month = (Number) result[1];
            Number count = (Number) result[2];

            trends.add(new TrendDataDTO(
                    year.intValue() + "-" + String.format("%02d", month.intValue()),
                    count.longValue()
            ));
        }

        return trends;
    }

@Override
public List<DriverPerformanceDTO> getDriverPerformance() {
    List<Object[]> results = routeRepository.findDriverPerformanceMetrics();
    List<DriverPerformanceDTO> metrics = new ArrayList<>();

    for (Object[] result : results) {
        Number driverId = (Number) result[0];
        String driverName = (String) result[1];
        Number totalRoutes = (Number) result[2];
        Number completedRoutes = (Number) result[3];
        Number inProgressRoutes = (Number) result[4];
        Number plannedRoutes = (Number) result[5];

        metrics.add(new DriverPerformanceDTO(
                driverId.intValue(),
                driverName,
                totalRoutes.longValue(),
                completedRoutes.longValue(),
                inProgressRoutes.longValue(),
                plannedRoutes.longValue()
        ));
    }

    return metrics;
}
}