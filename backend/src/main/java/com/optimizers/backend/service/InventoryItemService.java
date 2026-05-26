// service/InventoryItemService.java
package com.optimizers.backend.service;

import java.util.List;
import com.optimizers.backend.dto.request.InventoryItemRequestDTO;
import com.optimizers.backend.dto.response.InventoryItemResponseDTO;

public interface InventoryItemService {
    InventoryItemResponseDTO createItem(InventoryItemRequestDTO requestDTO);
    InventoryItemResponseDTO getItemById(Integer id);
    InventoryItemResponseDTO getItemBySku(String sku);
    List<InventoryItemResponseDTO> getAllItems();
    List<InventoryItemResponseDTO> getItemsByWarehouse(Integer warehouseId);
    List<InventoryItemResponseDTO> getItemsByCategory(String category);
    InventoryItemResponseDTO updateItem(Integer id, InventoryItemRequestDTO requestDTO);
    void deleteItem(Integer id);
}