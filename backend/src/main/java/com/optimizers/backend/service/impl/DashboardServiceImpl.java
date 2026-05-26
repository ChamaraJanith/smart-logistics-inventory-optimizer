package com.optimizers.backend.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.response.DashboardSummaryDTO;
import com.optimizers.backend.dto.response.RecentDeliveryDTO;
import com.optimizers.backend.dto.response.RecentRouteDTO;
import com.optimizers.backend.dto.response.StatusCountDTO;
import com.optimizers.backend.dto.response.TodayPerformanceDTO;
import com.optimizers.backend.entity.Delivery;
import com.optimizers.backend.entity.Route;
import com.optimizers.backend.repository.DeliveryRepository;
import com.optimizers.backend.repository.RouteRepository;
import com.optimizers.backend.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        long totalDeliveries = deliveryRepository.count();
        long pendingDeliveries = deliveryRepository.countByStatus("PENDING");
        long assignedDeliveries = deliveryRepository.countByStatus("ASSIGNED");
        long inTransitDeliveries = deliveryRepository.countByStatus("IN_TRANSIT");
        long deliveredDeliveries = deliveryRepository.countByStatus("DELIVERED");

        long totalRoutes = routeRepository.count();
        long plannedRoutes = routeRepository.countByRouteStatus("PLANNED");
        long inProgressRoutes = routeRepository.countByRouteStatus("IN_PROGRESS");
        long completedRoutes = routeRepository.countByRouteStatus("COMPLETED");

        long todayRoutes = routeRepository.countByRouteDate(today);
        long todayDeliveries = deliveryRepository.countByRequestedDateBetween(startOfDay, endOfDay);

        return new DashboardSummaryDTO(
                totalDeliveries,
                pendingDeliveries,
                assignedDeliveries,
                inTransitDeliveries,
                deliveredDeliveries,
                totalRoutes,
                plannedRoutes,
                inProgressRoutes,
                completedRoutes,
                todayRoutes,
                todayDeliveries
        );
    }

    @Override
    public List<StatusCountDTO> getDeliveryStatusCounts() {
        return Arrays.asList(
                new StatusCountDTO("PENDING", deliveryRepository.countByStatus("PENDING")),
                new StatusCountDTO("ASSIGNED", deliveryRepository.countByStatus("ASSIGNED")),
                new StatusCountDTO("IN_TRANSIT", deliveryRepository.countByStatus("IN_TRANSIT")),
                new StatusCountDTO("DELIVERED", deliveryRepository.countByStatus("DELIVERED"))
        );
    }

    @Override
    public List<StatusCountDTO> getRouteStatusCounts() {
        return Arrays.asList(
                new StatusCountDTO("PLANNED", routeRepository.countByRouteStatus("PLANNED")),
                new StatusCountDTO("IN_PROGRESS", routeRepository.countByRouteStatus("IN_PROGRESS")),
                new StatusCountDTO("COMPLETED", routeRepository.countByRouteStatus("COMPLETED"))
        );
    }

    @Override
    public List<RecentRouteDTO> getRecentRoutes() {
        return routeRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToRecentRouteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecentDeliveryDTO> getRecentDeliveries() {
        return deliveryRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToRecentDeliveryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TodayPerformanceDTO getTodayPerformance() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        List<Route> todayRoutesList = routeRepository.findByRouteDate(today);
        List<Delivery> todayDeliveriesList = deliveryRepository.findByRequestedDateBetween(startOfDay, endOfDay);

        long todayRoutes = todayRoutesList.size();
        long completedRoutes = todayRoutesList.stream()
                .filter(route -> "COMPLETED".equals(route.getRouteStatus()))
                .count();
        long inProgressRoutes = todayRoutesList.stream()
                .filter(route -> "IN_PROGRESS".equals(route.getRouteStatus()))
                .count();

        long todayDeliveries = todayDeliveriesList.size();
        long deliveredDeliveries = todayDeliveriesList.stream()
                .filter(delivery -> "DELIVERED".equals(delivery.getStatus()))
                .count();
        long pendingDeliveries = todayDeliveriesList.stream()
                .filter(delivery -> "PENDING".equals(delivery.getStatus()))
                .count();

        return new TodayPerformanceDTO(
                todayRoutes,
                completedRoutes,
                inProgressRoutes,
                todayDeliveries,
                deliveredDeliveries,
                pendingDeliveries
        );
    }

    private RecentRouteDTO mapToRecentRouteDTO(Route route) {
        return new RecentRouteDTO(
                route.getRouteId(),
                route.getVehicle() != null ? route.getVehicle().getVehicleNumber() : null,
                route.getDriver() != null ? route.getDriver().getDriverName() : null,
                route.getStartLocation(),
                route.getEndLocation(),
                route.getRouteStatus(),
                route.getRouteDate(),
                route.getTotalDistanceKm() != null ? route.getTotalDistanceKm().doubleValue() : null,
                route.getCreatedAt()
        );
    }

    private RecentDeliveryDTO mapToRecentDeliveryDTO(Delivery delivery) {
        return new RecentDeliveryDTO(
                delivery.getDeliveryId(),
                delivery.getCustomerName(),
                delivery.getDeliveryAddress(),
                delivery.getStatus(),
                delivery.getRequestedDate(),
                delivery.getCreatedAt()
        );
    }
}