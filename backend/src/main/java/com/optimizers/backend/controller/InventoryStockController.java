// controller/InventoryStockController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.request.InventoryStockRequestDTO;
import com.optimizers.backend.dto.request.StockUpdateRequestDTO;
import com.optimizers.backend.dto.response.InventoryStockResponseDTO;
import com.optimizers.backend.service.InventoryStockService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/inventory-stock")
@CrossOrigin(origins = "*")
public class InventoryStockController {

    @Autowired
    private InventoryStockService stockService;

    @PostMapping
    public ResponseEntity<InventoryStockResponseDTO> createStock(
            @Valid @RequestBody InventoryStockRequestDTO requestDTO) {
        return new ResponseEntity<>(stockService.createStock(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryStockResponseDTO>> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<InventoryStockResponseDTO> getStockById(@PathVariable Integer stockId) {
        return ResponseEntity.ok(stockService.getStockById(stockId));
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<InventoryStockResponseDTO> getStockByItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(stockService.getStockByItemId(itemId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryStockResponseDTO>> getStockByWarehouse(
            @PathVariable Integer warehouseId) {
        return ResponseEntity.ok(stockService.getStockByWarehouse(warehouseId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryStockResponseDTO>> getLowStock() {
        return ResponseEntity.ok(stockService.getLowStockItems());
    }

    @GetMapping("/low-stock/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryStockResponseDTO>> getLowStockByWarehouse(
            @PathVariable Integer warehouseId) {
        return ResponseEntity.ok(stockService.getLowStockByWarehouse(warehouseId));
    }

    // Stock update endpoint — RESTOCK, DISPATCH, ADJUSTMENT, RESERVE, RELEASE_RESERVE
    @PatchMapping("/{stockId}/update")
    public ResponseEntity<InventoryStockResponseDTO> updateStock(
            @PathVariable Integer stockId,
            @Valid @RequestBody StockUpdateRequestDTO updateDTO) {
        return ResponseEntity.ok(stockService.updateStock(stockId, updateDTO));
    }
}