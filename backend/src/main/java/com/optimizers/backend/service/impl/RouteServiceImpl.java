package com.optimizers.backend.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.request.RouteRequestDTO;
import com.optimizers.backend.dto.response.RouteDeliveryDetailDTO;
import com.optimizers.backend.dto.response.RouteResponseDTO;
import com.optimizers.backend.dto.response.RouteSummaryDTO;
import com.optimizers.backend.entity.Driver;
import com.optimizers.backend.entity.Route;
import com.optimizers.backend.entity.RouteDelivery;
import com.optimizers.backend.entity.Vehicle;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.DriverRepository;
import com.optimizers.backend.repository.RouteDeliveryRepository;
import com.optimizers.backend.repository.RouteRepository;
import com.optimizers.backend.repository.VehicleRepository;
import com.optimizers.backend.service.RouteService;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RouteDeliveryRepository routeDeliveryRepository;

    @Override
    public RouteResponseDTO createRoute(RouteRequestDTO requestDTO) {
        Route route = new Route();
        mapToEntity(route, requestDTO);
        Route savedRoute = routeRepository.save(route);
        return mapToResponseDTO(savedRoute);
    }

    @Override
    public RouteResponseDTO getRouteById(Integer id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
        return mapToResponseDTO(route);
    }

    @Override
    public List<RouteResponseDTO> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponseDTO> getRoutesByDate(LocalDate routeDate) {
        return routeRepository.findByRouteDate(routeDate)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteResponseDTO> getRoutesByStatus(String status) {
        return routeRepository.findByRouteStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteResponseDTO updateRoute(Integer id, RouteRequestDTO requestDTO) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));

        mapToEntity(route, requestDTO);
        Route updatedRoute = routeRepository.save(route);
        return mapToResponseDTO(updatedRoute);
    }

    @Override
    public void deleteRoute(Integer id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + id));
        routeRepository.delete(route);
    }

    @Override
    public RouteSummaryDTO getRouteSummary(Integer routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        List<RouteDelivery> routeDeliveries = routeDeliveryRepository
                .findByRouteRouteIdOrderByStopSequenceAsc(routeId);

        List<RouteDeliveryDetailDTO> deliveryDetails = routeDeliveries.stream()
                .map(rd -> new RouteDeliveryDetailDTO(
                        rd.getRouteDeliveryId(),
                        rd.getDelivery().getDeliveryId(),
                        rd.getStopSequence(),
                        rd.getDelivery().getCustomerName(),
                        rd.getDelivery().getContactNumber(),
                        rd.getDelivery().getDeliveryAddress(),
                        rd.getDelivery().getLatitude(),
                        rd.getDelivery().getLongitude(),
                        rd.getDelivery().getPackageWeight(),
                        rd.getDelivery().getPackageVolume(),
                        rd.getDelivery().getPriority(),
                        rd.getPredictedEta(),
                        rd.getEstimatedArrivalTime(),
                        rd.getActualArrivalTime(),
                        rd.getStopStatus()
                ))
                .collect(Collectors.toList());

        Integer driverId = null;
        if (route.getDriver() != null) {
            driverId = route.getDriver().getDriverId();
        }

        return new RouteSummaryDTO(
                route.getRouteId(),
                route.getVehicle().getVehicleId(),
                driverId,
                route.getRouteDate(),
                route.getStartLocation(),
                route.getEndLocation(),
                route.getRouteStatus(),
                deliveryDetails
        );
    }

    private void mapToEntity(Route route, RouteRequestDTO requestDTO) {
        Vehicle vehicle = vehicleRepository.findById(requestDTO.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + requestDTO.getVehicleId()));
        route.setVehicle(vehicle);

        if (requestDTO.getDriverId() != null) {
            Driver driver = driverRepository.findById(requestDTO.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + requestDTO.getDriverId()));
            route.setDriver(driver);
        } else {
            route.setDriver(null);
        }

        route.setRouteDate(requestDTO.getRouteDate());
        route.setStartWarehouseId(requestDTO.getStartWarehouseId());
        route.setStartLocation(requestDTO.getStartLocation());
        route.setEndLocation(requestDTO.getEndLocation());
        route.setTotalDistanceKm(requestDTO.getTotalDistanceKm());
        route.setEstimatedDurationMin(requestDTO.getEstimatedDurationMin());
        route.setPredictedCost(requestDTO.getPredictedCost());
        route.setPredictedDelayRisk(requestDTO.getPredictedDelayRisk());
        route.setOptimizationScore(requestDTO.getOptimizationScore());
        route.setRouteStatus(requestDTO.getRouteStatus());
    }

    private RouteResponseDTO mapToResponseDTO(Route route) {
        Integer driverId = null;
        String driverName = null;

        if (route.getDriver() != null) {
            driverId = route.getDriver().getDriverId();
            driverName = route.getDriver().getDriverName();
        }

        return new RouteResponseDTO(
                route.getRouteId(),
                route.getVehicle().getVehicleId(),
                route.getVehicle().getVehicleNumber(),
                driverId,
                driverName,
                route.getRouteDate(),
                route.getStartWarehouseId(),
                route.getStartLocation(),
                route.getEndLocation(),
                route.getTotalDistanceKm(),
                route.getEstimatedDurationMin(),
                route.getPredictedCost(),
                route.getPredictedDelayRisk(),
                route.getOptimizationScore(),
                route.getRouteStatus(),
                route.getCreatedAt()
        );
    }
}