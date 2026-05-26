// service/InventoryStockService.java
package com.optimizers.backend.service;

import java.util.List;
import com.optimizers.backend.dto.request.InventoryStockRequestDTO;
import com.optimizers.backend.dto.request.StockUpdateRequestDTO;
import com.optimizers.backend.dto.response.InventoryStockResponseDTO;

public interface InventoryStockService {
    InventoryStockResponseDTO createStock(InventoryStockRequestDTO requestDTO);
    InventoryStockResponseDTO getStockById(Integer stockId);
    InventoryStockResponseDTO getStockByItemId(Integer itemId);
    List<InventoryStockResponseDTO> getAllStock();
    List<InventoryStockResponseDTO> getStockByWarehouse(Integer warehouseId);
    List<InventoryStockResponseDTO> getLowStockItems();
    List<InventoryStockResponseDTO> getLowStockByWarehouse(Integer warehouseId);
    InventoryStockResponseDTO updateStock(Integer stockId, StockUpdateRequestDTO updateDTO);
}