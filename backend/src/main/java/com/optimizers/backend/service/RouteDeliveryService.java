package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.RouteDeliveryRequestDTO;
import com.optimizers.backend.dto.response.RouteDeliveryResponseDTO;

public interface RouteDeliveryService {
    RouteDeliveryResponseDTO assignDeliveryToRoute(RouteDeliveryRequestDTO requestDTO);
    RouteDeliveryResponseDTO getById(Integer id);
    List<RouteDeliveryResponseDTO> getAll();
    List<RouteDeliveryResponseDTO> getByRouteId(Integer routeId);
    List<RouteDeliveryResponseDTO> getByDeliveryId(Integer deliveryId);
    RouteDeliveryResponseDTO update(Integer id, RouteDeliveryRequestDTO requestDTO);
    void delete(Integer id);
}