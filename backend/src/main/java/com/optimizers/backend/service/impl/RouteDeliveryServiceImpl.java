package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optimizers.backend.dto.request.RouteDeliveryRequestDTO;
import com.optimizers.backend.dto.request.RouteDeliveryStatusUpdateDTO;
import com.optimizers.backend.dto.response.RouteDeliveryResponseDTO;
import com.optimizers.backend.entity.Delivery;
import com.optimizers.backend.entity.Route;
import com.optimizers.backend.entity.RouteDelivery;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.DeliveryRepository;
import com.optimizers.backend.repository.RouteDeliveryRepository;
import com.optimizers.backend.repository.RouteRepository;
import com.optimizers.backend.service.RouteDeliveryService;

@Service
public class RouteDeliveryServiceImpl implements RouteDeliveryService {

    @Autowired
    private RouteDeliveryRepository routeDeliveryRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public RouteDeliveryResponseDTO assignDeliveryToRoute(RouteDeliveryRequestDTO requestDTO) {
        if (routeDeliveryRepository.existsByRouteRouteIdAndDeliveryDeliveryId(
                requestDTO.getRouteId(), requestDTO.getDeliveryId())) {
            throw new IllegalArgumentException("This delivery is already assigned to the route");
        }

        if (routeDeliveryRepository.existsByRouteRouteIdAndStopSequence(
                requestDTO.getRouteId(), requestDTO.getStopSequence())) {
            throw new IllegalArgumentException("Stop sequence already exists for this route");
        }

        Route route = routeRepository.findById(requestDTO.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + requestDTO.getRouteId()));

        Delivery delivery = deliveryRepository.findById(requestDTO.getDeliveryId())
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + requestDTO.getDeliveryId()));

        RouteDelivery routeDelivery = new RouteDelivery();
        routeDelivery.setRoute(route);
        routeDelivery.setDelivery(delivery);
        routeDelivery.setStopSequence(requestDTO.getStopSequence());
        routeDelivery.setPredictedEta(requestDTO.getPredictedEta());
        routeDelivery.setEstimatedArrivalTime(requestDTO.getEstimatedArrivalTime());
        routeDelivery.setActualArrivalTime(requestDTO.getActualArrivalTime());
        routeDelivery.setStopStatus(requestDTO.getStopStatus());

        RouteDelivery saved = routeDeliveryRepository.save(routeDelivery);

        delivery.setStatus("ASSIGNED");
        deliveryRepository.save(delivery);

        updateRouteStatusBasedOnStops(route.getRouteId());

        return mapToResponse(saved);
    }

    @Override
    public RouteDeliveryResponseDTO getById(Integer id) {
        RouteDelivery routeDelivery = routeDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RouteDelivery not found with id: " + id));
        return mapToResponse(routeDelivery);
    }

    @Override
    public List<RouteDeliveryResponseDTO> getAll() {
        return routeDeliveryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteDeliveryResponseDTO> getByRouteId(Integer routeId) {
        return routeDeliveryRepository.findByRouteRouteId(routeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteDeliveryResponseDTO> getByDeliveryId(Integer deliveryId) {
        return routeDeliveryRepository.findByDeliveryDeliveryId(deliveryId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RouteDeliveryResponseDTO updateStopStatus(Integer id, RouteDeliveryStatusUpdateDTO requestDTO) {
        RouteDelivery routeDelivery = routeDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RouteDelivery not found with id: " + id));

        String newStatus = requestDTO.getStopStatus().trim().toUpperCase();
        routeDelivery.setStopStatus(newStatus);

        if (requestDTO.getActualArrivalTime() != null) {
            routeDelivery.setActualArrivalTime(requestDTO.getActualArrivalTime());
        }

        Delivery delivery = routeDelivery.getDelivery();

        if ("DELIVERED".equals(newStatus)) {
            delivery.setStatus("DELIVERED");
        } else if ("IN_PROGRESS".equals(newStatus)) {
            delivery.setStatus("IN_TRANSIT");
        } else if ("PENDING".equals(newStatus)) {
            delivery.setStatus("ASSIGNED");
        }

        RouteDelivery updated = routeDeliveryRepository.save(routeDelivery);
        deliveryRepository.save(delivery);

        updateRouteStatusBasedOnStops(routeDelivery.getRoute().getRouteId());

        return mapToResponse(updated);
    }

    private void updateRouteStatusBasedOnStops(Integer routeId) {
        long totalStops = routeDeliveryRepository.countByRouteRouteId(routeId);
        long deliveredStops = routeDeliveryRepository.countByRouteRouteIdAndStopStatus(routeId, "DELIVERED");
        boolean hasInProgress = routeDeliveryRepository.existsByRouteRouteIdAndStopStatus(routeId, "IN_PROGRESS");

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + routeId));

        if (totalStops > 0 && deliveredStops == totalStops) {
            route.setRouteStatus("COMPLETED");
        } else if (hasInProgress) {
            route.setRouteStatus("IN_PROGRESS");
        } else {
            route.setRouteStatus("PLANNED");
        }

        routeRepository.save(route);
    }

    @Override
    public RouteDeliveryResponseDTO update(Integer id, RouteDeliveryRequestDTO requestDTO) {
        RouteDelivery routeDelivery = routeDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RouteDelivery not found with id: " + id));

        if (!routeDelivery.getRoute().getRouteId().equals(requestDTO.getRouteId())) {
            Route route = routeRepository.findById(requestDTO.getRouteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: " + requestDTO.getRouteId()));
            routeDelivery.setRoute(route);
        }

        if (!routeDelivery.getDelivery().getDeliveryId().equals(requestDTO.getDeliveryId())) {
            Delivery delivery = deliveryRepository.findById(requestDTO.getDeliveryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + requestDTO.getDeliveryId()));
            routeDelivery.setDelivery(delivery);
        }

        routeDelivery.setStopSequence(requestDTO.getStopSequence());
        routeDelivery.setPredictedEta(requestDTO.getPredictedEta());
        routeDelivery.setEstimatedArrivalTime(requestDTO.getEstimatedArrivalTime());
        routeDelivery.setActualArrivalTime(requestDTO.getActualArrivalTime());
        routeDelivery.setStopStatus(requestDTO.getStopStatus());

        RouteDelivery updated = routeDeliveryRepository.save(routeDelivery);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        RouteDelivery routeDelivery = routeDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RouteDelivery not found with id: " + id));

        Delivery delivery = routeDelivery.getDelivery();

        routeDeliveryRepository.delete(routeDelivery);

        delivery.setStatus("PENDING");
        deliveryRepository.save(delivery);

        updateRouteStatusBasedOnStops(routeDelivery.getRoute().getRouteId());
    }

    private RouteDeliveryResponseDTO mapToResponse(RouteDelivery routeDelivery) {
        return new RouteDeliveryResponseDTO(
                routeDelivery.getRouteDeliveryId(),
                routeDelivery.getRoute().getRouteId(),
                routeDelivery.getDelivery().getDeliveryId(),
                routeDelivery.getStopSequence(),
                routeDelivery.getPredictedEta(),
                routeDelivery.getEstimatedArrivalTime(),
                routeDelivery.getActualArrivalTime(),
                routeDelivery.getStopStatus(),
                routeDelivery.getDelivery().getCustomerName(),
                routeDelivery.getDelivery().getDeliveryAddress()
        );
    }
}