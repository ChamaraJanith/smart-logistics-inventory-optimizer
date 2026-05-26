// repository/InventoryStockRepository.java
package com.optimizers.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.optimizers.backend.entity.InventoryStock;

public interface InventoryStockRepository extends JpaRepository<InventoryStock, Integer> {

    Optional<InventoryStock> findByItem_ItemId(Integer itemId);

    List<InventoryStock> findByWarehouse_WarehouseId(Integer warehouseId);

    // Low stock items — available <= reorder level
    @Query("SELECT s FROM InventoryStock s WHERE s.availableQuantity <= s.reorderLevel " +
           "AND s.item.status = 'ACTIVE'")
    List<InventoryStock> findLowStockItems();

    // Low stock by warehouse
    @Query("SELECT s FROM InventoryStock s WHERE s.warehouse.warehouseId = :warehouseId " +
           "AND s.availableQuantity <= s.reorderLevel AND s.item.status = 'ACTIVE'")
    List<InventoryStock> findLowStockByWarehouse(@Param("warehouseId") Integer warehouseId);
}