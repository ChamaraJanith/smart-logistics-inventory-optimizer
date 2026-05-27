// service/impl/StockTransactionServiceImpl.java
package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.response.StockTransactionResponseDTO;
import com.optimizers.backend.entity.StockTransaction;
import com.optimizers.backend.repository.StockTransactionRepository;
import com.optimizers.backend.service.StockTransactionService;

@Service
public class StockTransactionServiceImpl
        implements StockTransactionService {

    @Autowired
    private StockTransactionRepository transactionRepository;

    @Override
    public List<StockTransactionResponseDTO> getAllTransactions() {
        // Return all transactions ordered by latest first
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransactionResponseDTO> getTransactionsByItem(
            Integer itemId) {
        // Return all transactions for a specific item
        return transactionRepository
                .findByItem_ItemIdOrderByCreatedAtDesc(itemId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransactionResponseDTO> getTransactionsByWarehouse(
            Integer warehouseId) {
        // Return all transactions for a specific warehouse
        return transactionRepository
                .findByWarehouse_WarehouseIdOrderByCreatedAtDesc(
                        warehouseId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockTransactionResponseDTO> getTransactionsByType(
            String transactionType) {
        // Return all transactions filtered by type
        return transactionRepository
                .findByTransactionTypeOrderByCreatedAtDesc(
                        transactionType.toUpperCase())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Map entity to response DTO
    private StockTransactionResponseDTO mapToResponseDTO(
            StockTransaction txn) {

        StockTransactionResponseDTO dto =
                new StockTransactionResponseDTO();

        dto.setTransactionId(txn.getTransactionId());
        dto.setStockId(txn.getStock().getStockId());
        dto.setItemId(txn.getItem().getItemId());
        dto.setItemName(txn.getItem().getItemName());
        dto.setSku(txn.getItem().getSku());
        dto.setWarehouseId(txn.getWarehouse().getWarehouseId());
        dto.setWarehouseName(txn.getWarehouse().getName());
        dto.setTransactionType(txn.getTransactionType());
        dto.setQuantity(txn.getQuantity());
        dto.setQuantityBefore(txn.getQuantityBefore());
        dto.setQuantityAfter(txn.getQuantityAfter());
        dto.setPerformedBy(txn.getPerformedBy());
        dto.setNotes(txn.getNotes());
        dto.setCreatedAt(txn.getCreatedAt());
        return dto;
    }
}