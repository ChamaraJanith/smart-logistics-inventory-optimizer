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
}