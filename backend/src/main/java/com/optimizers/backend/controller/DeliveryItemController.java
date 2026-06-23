package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.request.DeliveryItemRequestDTO;
import com.optimizers.backend.dto.response.DeliveryItemResponseDTO;
import com.optimizers.backend.service.DeliveryItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/deliveries/{deliveryId}/items")
@CrossOrigin(origins = "*")
public class DeliveryItemController {

    @Autowired
    private DeliveryItemService deliveryItemService;

    @PostMapping
    public ResponseEntity<DeliveryItemResponseDTO> addDeliveryItem(
            @PathVariable Integer deliveryId,
            @Valid @RequestBody DeliveryItemRequestDTO requestDTO) {
        return new ResponseEntity<>(deliveryItemService.addDeliveryItem(deliveryId, requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryItemResponseDTO>> getDeliveryItems(@PathVariable Integer deliveryId) {
        return ResponseEntity.ok(deliveryItemService.getDeliveryItems(deliveryId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<DeliveryItemResponseDTO> updateDeliveryItem(
            @PathVariable Integer deliveryId,
            @PathVariable Integer itemId,
            @Valid @RequestBody DeliveryItemRequestDTO requestDTO) {
        return ResponseEntity.ok(deliveryItemService.updateDeliveryItem(itemId, requestDTO));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeDeliveryItem(
            @PathVariable Integer deliveryId,
            @PathVariable Integer itemId) {
        deliveryItemService.removeDeliveryItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
