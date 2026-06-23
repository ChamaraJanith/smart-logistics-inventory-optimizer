package com.optimizers.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.request.RouteRequestDTO;
import com.optimizers.backend.dto.response.RouteResponseDTO;
import com.optimizers.backend.dto.response.RouteSummaryDTO;
import com.optimizers.backend.service.RouteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponseDTO> createRoute(@Valid @RequestBody RouteRequestDTO requestDTO) {
        return new ResponseEntity<>(routeService.createRoute(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> getRouteById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @GetMapping
    public ResponseEntity<List<RouteResponseDTO>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/date/{routeDate}")
    public ResponseEntity<List<RouteResponseDTO>> getRoutesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate routeDate) {
        return ResponseEntity.ok(routeService.getRoutesByDate(routeDate));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RouteResponseDTO>> getRoutesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(routeService.getRoutesByStatus(status));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<RouteSummaryDTO> getRouteSummary(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.getRouteSummary(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> updateRoute(
            @PathVariable Integer id,
            @Valid @RequestBody RouteRequestDTO requestDTO) {
        return ResponseEntity.ok(routeService.updateRoute(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Integer id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stock-validation")
    public ResponseEntity<com.optimizers.backend.dto.response.RouteStockValidationDTO> validateRouteStock(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.validateRouteStock(id));
    }

    @PostMapping("/{id}/allocate-stock")
    public ResponseEntity<Void> allocateRouteStock(@PathVariable Integer id) {
        routeService.allocateRouteStock(id);
        return ResponseEntity.ok().build();
    }
}