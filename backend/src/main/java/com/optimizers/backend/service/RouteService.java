package com.optimizers.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.optimizers.backend.dto.request.RouteRequestDTO;
import com.optimizers.backend.dto.response.RouteResponseDTO;
import com.optimizers.backend.dto.response.RouteSummaryDTO;

public interface RouteService {
    RouteResponseDTO createRoute(RouteRequestDTO requestDTO);
    RouteResponseDTO getRouteById(Integer id);
    List<RouteResponseDTO> getAllRoutes();
    List<RouteResponseDTO> getRoutesByDate(LocalDate routeDate);
    List<RouteResponseDTO> getRoutesByStatus(String status);
    RouteResponseDTO updateRoute(Integer id, RouteRequestDTO requestDTO);
    void deleteRoute(Integer id);
    RouteSummaryDTO getRouteSummary(Integer routeId);
}