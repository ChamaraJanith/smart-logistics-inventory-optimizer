package com.optimizers.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.optimizers.backend.dto.request.RouteRequestDTO;
import com.optimizers.backend.dto.response.RouteResponseDTO;
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
        RouteResponseDTO response = routeService.createRoute(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RouteResponseDTO>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> getRouteById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<RouteResponseDTO>> getRoutesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(routeService.getRoutesByDate(date));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<RouteResponseDTO>> getRoutesByStatus(@RequestParam String status) {
        return ResponseEntity.ok(routeService.getRoutesByStatus(status));
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
}