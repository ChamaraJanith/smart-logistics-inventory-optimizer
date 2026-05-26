// service/impl/InventoryStockServiceImpl.java
package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optimizers.backend.dto.request.InventoryStockRequestDTO;
import com.optimizers.backend.dto.request.StockUpdateRequestDTO;
import com.optimizers.backend.dto.response.InventoryStockResponseDTO;
import com.optimizers.backend.entity.InventoryItem;
import com.optimizers.backend.entity.InventoryStock;
import com.optimizers.backend.entity.StockTransaction;
import com.optimizers.backend.entity.Warehouse;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.InventoryItemRepository;
import com.optimizers.backend.repository.InventoryStockRepository;
import com.optimizers.backend.repository.StockTransactionRepository;
import com.optimizers.backend.repository.WarehouseRepository;
import com.optimizers.backend.service.InventoryStockService;
import com.optimizers.backend.service.ReorderAlertService;
import com.optimizers.backend.service.AnomalyDetectionService;

@Service
public class InventoryStockServiceImpl implements InventoryStockService {

    @Autowired private InventoryStockRepository stockRepository;
    @Autowired private InventoryItemRepository itemRepository;
    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private StockTransactionRepository transactionRepository;
    @Autowired private ReorderAlertService reorderAlertService;
    @Autowired private AnomalyDetectionService anomalyDetectionService;

    @Override
    public InventoryStockResponseDTO createStock(InventoryStockRequestDTO requestDTO) {
        InventoryItem item = itemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + requestDTO.getItemId()));

        Warehouse warehouse = warehouseRepository.findById(requestDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + requestDTO.getWarehouseId()));

        InventoryStock stock = new InventoryStock();
        stock.setItem(item);
        stock.setWarehouse(warehouse);
        stock.setQuantityOnHand(requestDTO.getQuantityOnHand());
        stock.setReservedQuantity(requestDTO.getReservedQuantity() != null
                ? requestDTO.getReservedQuantity() : BigDecimal.ZERO);
        stock.setReorderLevel(requestDTO.getReorderLevel());
        stock.setReorderQuantity(requestDTO.getReorderQuantity());
        stock.setMaxStockLevel(requestDTO.getMaxStockLevel());

        return mapToResponseDTO(stockRepository.save(stock));
    }

    @Override
    public InventoryStockResponseDTO getStockById(Integer stockId) {
        return mapToResponseDTO(stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + stockId)));
    }

    @Override
    public InventoryStockResponseDTO getStockByItemId(Integer itemId) {
        return mapToResponseDTO(stockRepository.findByItem_ItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for item: " + itemId)));
    }

    @Override
    public List<InventoryStockResponseDTO> getAllStock() {
        return stockRepository.findAll().stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<InventoryStockResponseDTO> getStockByWarehouse(Integer warehouseId) {
        return stockRepository.findByWarehouse_WarehouseId(warehouseId).stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<InventoryStockResponseDTO> getLowStockItems() {
        return stockRepository.findLowStockItems().stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<InventoryStockResponseDTO> getLowStockByWarehouse(Integer warehouseId) {
        return stockRepository.findLowStockByWarehouse(warehouseId).stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryStockResponseDTO updateStock(Integer stockId, StockUpdateRequestDTO updateDTO) {
        InventoryStock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + stockId));

        BigDecimal quantityBefore = stock.getQuantityOnHand();
        BigDecimal quantityAfter;

        switch (updateDTO.getTransactionType().toUpperCase()) {
            case "RESTOCK":
                quantityAfter = quantityBefore.add(updateDTO.getQuantity());
                stock.setQuantityOnHand(quantityAfter);
                break;
            case "DISPATCH":
                quantityAfter = quantityBefore.subtract(updateDTO.getQuantity());
                if (quantityAfter.compareTo(BigDecimal.ZERO) < 0)
                    throw new IllegalArgumentException("Insufficient stock for dispatch");
                stock.setQuantityOnHand(quantityAfter);
                break;
            case "ADJUSTMENT":
                quantityAfter = updateDTO.getQuantity(); // direct set
                stock.setQuantityOnHand(quantityAfter);
                break;
            case "RESERVE":
                BigDecimal newReserved = stock.getReservedQuantity().add(updateDTO.getQuantity());
                stock.setReservedQuantity(newReserved);
                quantityAfter = stock.getQuantityOnHand();
                break;
            case "RELEASE_RESERVE":
                BigDecimal released = stock.getReservedQuantity().subtract(updateDTO.getQuantity());
                if (released.compareTo(BigDecimal.ZERO) < 0)
                    throw new IllegalArgumentException("Cannot release more than reserved quantity");
                stock.setReservedQuantity(released);
                quantityAfter = stock.getQuantityOnHand();
                break;
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + updateDTO.getTransactionType());
        }

        stock.setLastUpdated(LocalDateTime.now());
        InventoryStock saved = stockRepository.save(stock);

        // Log the transaction
        StockTransaction txn = new StockTransaction();
        txn.setStock(saved);
        txn.setItem(saved.getItem());
        txn.setWarehouse(saved.getWarehouse());
        txn.setTransactionType(updateDTO.getTransactionType().toUpperCase());
        txn.setQuantity(updateDTO.getQuantity());
        txn.setQuantityBefore(quantityBefore);
        txn.setQuantityAfter(quantityAfter);
        txn.setPerformedBy(updateDTO.getPerformedBy());
        txn.setNotes(updateDTO.getNotes());
        transactionRepository.save(txn);

        // Check & trigger reorder alert if needed
        reorderAlertService.checkAndTriggerAlert(saved);

        // Analyze for anomalies
        anomalyDetectionService.analyzeStockTransaction(saved, txn);

        return mapToResponseDTO(saved);
    }

    private String determineStockStatus(InventoryStock stock) {
        BigDecimal available = stock.getAvailableQuantity();
        if (available == null) return "UNKNOWN";
        if (available.compareTo(BigDecimal.ZERO) <= 0) return "OUT_OF_STOCK";
        BigDecimal halfReorder = stock.getReorderLevel().multiply(new BigDecimal("0.5"));
        if (available.compareTo(halfReorder) <= 0) return "CRITICAL";
        if (available.compareTo(stock.getReorderLevel()) <= 0) return "LOW";
        return "NORMAL";
    }

    private InventoryStockResponseDTO mapToResponseDTO(InventoryStock stock) {
        InventoryStockResponseDTO dto = new InventoryStockResponseDTO();
        dto.setStockId(stock.getStockId());
        dto.setItemId(stock.getItem().getItemId());
        dto.setItemName(stock.getItem().getItemName());
        dto.setSku(stock.getItem().getSku());
        dto.setWarehouseId(stock.getWarehouse().getWarehouseId());
        dto.setWarehouseName(stock.getWarehouse().getName());
        dto.setQuantityOnHand(stock.getQuantityOnHand());
        dto.setReservedQuantity(stock.getReservedQuantity());
        dto.setAvailableQuantity(stock.getAvailableQuantity());
        dto.setReorderLevel(stock.getReorderLevel());
        dto.setReorderQuantity(stock.getReorderQuantity());
        dto.setMaxStockLevel(stock.getMaxStockLevel());
        dto.setStockStatus(determineStockStatus(stock));
        dto.setLastUpdated(stock.getLastUpdated());
        return dto;
    }
}