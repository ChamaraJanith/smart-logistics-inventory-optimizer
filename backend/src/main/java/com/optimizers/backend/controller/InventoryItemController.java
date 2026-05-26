// controller/InventoryItemController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.request.InventoryItemRequestDTO;
import com.optimizers.backend.dto.response.InventoryItemResponseDTO;
import com.optimizers.backend.service.InventoryItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/inventory-items")
@CrossOrigin(origins = "*")
public class InventoryItemController {

    @Autowired
    private InventoryItemService itemService;

    @PostMapping
    public ResponseEntity<InventoryItemResponseDTO> createItem(
            @Valid @RequestBody InventoryItemRequestDTO requestDTO) {
        return new ResponseEntity<>(itemService.createItem(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> getItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryItemResponseDTO> getItemBySku(@PathVariable String sku) {
        return ResponseEntity.ok(itemService.getItemBySku(sku));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryItemResponseDTO>> getItemsByWarehouse(
            @PathVariable Integer warehouseId) {
        return ResponseEntity.ok(itemService.getItemsByWarehouse(warehouseId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<InventoryItemResponseDTO>> getItemsByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponseDTO> updateItem(
            @PathVariable Integer id,
            @Valid @RequestBody InventoryItemRequestDTO requestDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}