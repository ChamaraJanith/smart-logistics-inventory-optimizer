// controller/StockTransactionController.java
package com.optimizers.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimizers.backend.dto.response.StockTransactionResponseDTO;
import com.optimizers.backend.service.StockTransactionService;

@RestController
@RequestMapping("/api/v1/stock-transactions")
@CrossOrigin(origins = "*")
public class StockTransactionController {

    @Autowired
    private StockTransactionService transactionService;

    // Get all stock transactions
    @GetMapping
    public ResponseEntity<List<StockTransactionResponseDTO>>
            getAllTransactions() {
        return ResponseEntity.ok(
                transactionService.getAllTransactions());
    }

    // Get transactions by item id
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<StockTransactionResponseDTO>>
            getByItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByItem(itemId));
    }

    // Get transactions by warehouse id
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<StockTransactionResponseDTO>>
            getByWarehouse(@PathVariable Integer warehouseId) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByWarehouse(
                        warehouseId));
    }

    // Get transactions by type
    // Types: RESTOCK, DISPATCH, ADJUSTMENT, RESERVE, RELEASE_RESERVE
    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<StockTransactionResponseDTO>>
            getByType(@PathVariable String transactionType) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByType(
                        transactionType));
    }
}