// controller/DemandForecastController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.request.DemandForecastRequestDTO;
import com.optimizers.backend.dto.response.DemandForecastResponseDTO;
import com.optimizers.backend.service.DemandForecastService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/demand-forecast")
@CrossOrigin(origins = "*")
public class DemandForecastController {

    @Autowired
    private DemandForecastService forecastService;

    // Single item forecast generate
    @PostMapping("/generate")
    public ResponseEntity<List<DemandForecastResponseDTO>> generateForecast(
            @Valid @RequestBody DemandForecastRequestDTO requestDTO) {
        return ResponseEntity.ok(
                forecastService.generateForecast(requestDTO));
    }

    // Whole warehouse forecast generate
    @PostMapping("/generate/warehouse/{warehouseId}")
    public ResponseEntity<List<DemandForecastResponseDTO>> generateForWarehouse(
            @PathVariable Integer warehouseId,
            @RequestParam(defaultValue = "7") Integer days) {
        return ResponseEntity.ok(
                forecastService.generateForecastForWarehouse(
                        warehouseId, days));
    }

    // Get saved forecasts
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<DemandForecastResponseDTO>> getByItem(
            @PathVariable Integer itemId) {
        return ResponseEntity.ok(
                forecastService.getForecastByItem(itemId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<DemandForecastResponseDTO>> getByWarehouse(
            @PathVariable Integer warehouseId) {
        return ResponseEntity.ok(
                forecastService.getForecastByWarehouse(warehouseId));
    }
}