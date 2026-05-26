// controller/ReorderAlertController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.response.ReorderAlertResponseDTO;
import com.optimizers.backend.service.ReorderAlertService;

@RestController
@RequestMapping("/api/v1/reorder-alerts")
@CrossOrigin(origins = "*")
public class ReorderAlertController {

    @Autowired
    private ReorderAlertService alertService;

    @GetMapping
    public ResponseEntity<List<ReorderAlertResponseDTO>> getAllOpenAlerts() {
        return ResponseEntity.ok(alertService.getAllOpenAlerts());
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ReorderAlertResponseDTO>> getAlertsByWarehouse(
            @PathVariable Integer warehouseId) {
        return ResponseEntity.ok(alertService.getOpenAlertsByWarehouse(warehouseId));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<ReorderAlertResponseDTO>> getAlertsBySeverity(
            @PathVariable String severity) {
        return ResponseEntity.ok(alertService.getAlertsBySeverity(severity));
    }

    @PatchMapping("/{alertId}/resolve")
    public ResponseEntity<ReorderAlertResponseDTO> resolveAlert(@PathVariable Integer alertId) {
        return ResponseEntity.ok(alertService.resolveAlert(alertId));
    }
}