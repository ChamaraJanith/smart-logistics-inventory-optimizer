// repository/InventoryItemRepository.java
package com.optimizers.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {
    List<InventoryItem> findByWarehouse_WarehouseId(Integer warehouseId);
    List<InventoryItem> findByStatus(String status);
    List<InventoryItem> findByWarehouse_WarehouseIdAndStatus(Integer warehouseId, String status);
    Optional<InventoryItem> findBySku(String sku);
    List<InventoryItem> findByCategory(String category);
}