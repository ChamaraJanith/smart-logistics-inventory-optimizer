package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.request.RouteDeliveryRequestDTO;
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
    public void delete(Integer id) {
        RouteDelivery routeDelivery = routeDeliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RouteDelivery not found with id: " + id));
        routeDeliveryRepository.delete(routeDelivery);
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