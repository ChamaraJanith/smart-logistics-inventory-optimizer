// service/impl/InventoryItemServiceImpl.java
package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.request.InventoryItemRequestDTO;
import com.optimizers.backend.dto.response.InventoryItemResponseDTO;
import com.optimizers.backend.entity.InventoryItem;
import com.optimizers.backend.entity.Warehouse;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.InventoryItemRepository;
import com.optimizers.backend.repository.WarehouseRepository;
import com.optimizers.backend.service.InventoryItemService;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    @Autowired
    private InventoryItemRepository itemRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public InventoryItemResponseDTO createItem(InventoryItemRequestDTO requestDTO) {
        Warehouse warehouse = warehouseRepository.findById(requestDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + requestDTO.getWarehouseId()));

        InventoryItem item = new InventoryItem();
        mapToEntity(item, requestDTO, warehouse);
        return mapToResponseDTO(itemRepository.save(item));
    }

    @Override
    public InventoryItemResponseDTO getItemById(Integer id) {
        return mapToResponseDTO(itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id)));
    }

    @Override
    public InventoryItemResponseDTO getItemBySku(String sku) {
        return mapToResponseDTO(itemRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with SKU: " + sku)));
    }

    @Override
    public List<InventoryItemResponseDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<InventoryItemResponseDTO> getItemsByWarehouse(Integer warehouseId) {
        return itemRepository.findByWarehouse_WarehouseId(warehouseId).stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<InventoryItemResponseDTO> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category).stream()
                .map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public InventoryItemResponseDTO updateItem(Integer id, InventoryItemRequestDTO requestDTO) {
        InventoryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

        Warehouse warehouse = warehouseRepository.findById(requestDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse not found with id: " + requestDTO.getWarehouseId()));

        mapToEntity(item, requestDTO, warehouse);
        return mapToResponseDTO(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Integer id) {
        InventoryItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        itemRepository.delete(item);
    }

    private void mapToEntity(InventoryItem item, InventoryItemRequestDTO dto, Warehouse warehouse) {
        item.setWarehouse(warehouse);
        item.setSku(dto.getSku());
        item.setItemName(dto.getItemName());
        item.setCategory(dto.getCategory());
        item.setUnit(dto.getUnit());
        item.setUnitWeightKg(dto.getUnitWeightKg());
        item.setUnitVolume(dto.getUnitVolume());
        item.setUnitPrice(dto.getUnitPrice());
        item.setStatus(dto.getStatus());
    }

    private InventoryItemResponseDTO mapToResponseDTO(InventoryItem item) {
        InventoryItemResponseDTO dto = new InventoryItemResponseDTO();
        dto.setItemId(item.getItemId());
        dto.setWarehouseId(item.getWarehouse().getWarehouseId());
        dto.setWarehouseName(item.getWarehouse().getName());
        dto.setSku(item.getSku());
        dto.setItemName(item.getItemName());
        dto.setCategory(item.getCategory());
        dto.setUnit(item.getUnit());
        dto.setUnitWeightKg(item.getUnitWeightKg());
        dto.setUnitVolume(item.getUnitVolume());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setStatus(item.getStatus());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }
}