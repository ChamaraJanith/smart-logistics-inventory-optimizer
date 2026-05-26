package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optimizers.backend.dto.request.RouteDeliveryRequestDTO;
import com.optimizers.backend.dto.request.RouteDeliveryStatusUpdateDTO;
import com.optimizers.backend.dto.response.RouteDeliveryResponseDTO;
import com.optimizers.backend.service.RouteDeliveryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/route-deliveries")
@CrossOrigin(origins = "*")
public class RouteDeliveryController {

    @Autowired
    private RouteDeliveryService routeDeliveryService;

    @PostMapping
    public ResponseEntity<RouteDeliveryResponseDTO> assignDeliveryToRoute(
            @Valid @RequestBody RouteDeliveryRequestDTO requestDTO) {
        return new ResponseEntity<>(routeDeliveryService.assignDeliveryToRoute(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RouteDeliveryResponseDTO>> getAll() {
        return ResponseEntity.ok(routeDeliveryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteDeliveryResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(routeDeliveryService.getById(id));
    }

    @GetMapping("/by-route/{routeId}")
    public ResponseEntity<List<RouteDeliveryResponseDTO>> getByRouteId(@PathVariable Integer routeId) {
        return ResponseEntity.ok(routeDeliveryService.getByRouteId(routeId));
    }
    @PatchMapping("/{id}/status")
public ResponseEntity<RouteDeliveryResponseDTO> updateStopStatus(
        @PathVariable Integer id,
        @Valid @RequestBody RouteDeliveryStatusUpdateDTO requestDTO) {
    return ResponseEntity.ok(routeDeliveryService.updateStopStatus(id, requestDTO));
}

    @GetMapping("/by-delivery/{deliveryId}")
    public ResponseEntity<List<RouteDeliveryResponseDTO>> getByDeliveryId(@PathVariable Integer deliveryId) {
        return ResponseEntity.ok(routeDeliveryService.getByDeliveryId(deliveryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteDeliveryResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody RouteDeliveryRequestDTO requestDTO) {
        return ResponseEntity.ok(routeDeliveryService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        routeDeliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}