// controller/AnomalyDetectionController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.response.AnomalyLogResponseDTO;
import com.optimizers.backend.service.AnomalyDetectionService;

@RestController
@RequestMapping("/api/v1/anomalies")
@CrossOrigin(origins = "*")
public class AnomalyDetectionController {

    @Autowired
    private AnomalyDetectionService anomalyService;

    @GetMapping
    public ResponseEntity<List<AnomalyLogResponseDTO>> getAllOpen() {
        return ResponseEntity.ok(
                anomalyService.getAllOpenAnomalies());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<AnomalyLogResponseDTO>> getByItem(
            @PathVariable Integer itemId) {
        return ResponseEntity.ok(
                anomalyService.getAnomaliesByItem(itemId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<AnomalyLogResponseDTO>> getByWarehouse(
            @PathVariable Integer warehouseId) {
        return ResponseEntity.ok(
                anomalyService.getAnomaliesByWarehouse(warehouseId));
    }

    @PatchMapping("/{anomalyId}/resolve")
    public ResponseEntity<AnomalyLogResponseDTO> resolve(
            @PathVariable Integer anomalyId) {
        return ResponseEntity.ok(
                anomalyService.resolveAnomaly(anomalyId));
    }
}