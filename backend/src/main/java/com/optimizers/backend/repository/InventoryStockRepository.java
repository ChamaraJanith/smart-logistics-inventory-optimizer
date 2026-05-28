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

       // Count out of stock items (available <= 0)
       @Query("SELECT COUNT(s) FROM InventoryStock s " +
              "WHERE s.availableQuantity <= 0 " +
              "AND s.item.status = 'ACTIVE'")
       long countOutOfStockItems();

       // Count out of stock by warehouse
       @Query("SELECT COUNT(s) FROM InventoryStock s " +
              "WHERE s.warehouse.warehouseId = :warehouseId " +
              "AND s.availableQuantity <= 0 " +
              "AND s.item.status = 'ACTIVE'")
       long countOutOfStockByWarehouse(
              @Param("warehouseId") Integer warehouseId);

       // Count low stock by warehouse
       @Query("SELECT COUNT(s) FROM InventoryStock s " +
              "WHERE s.warehouse.warehouseId = :warehouseId " +
              "AND s.availableQuantity <= s.reorderLevel " +
              "AND s.availableQuantity > 0 " +
              "AND s.item.status = 'ACTIVE'")
       long countLowStockByWarehouse(
              @Param("warehouseId") Integer warehouseId);

       // Count critical stock by warehouse (available <= reorderLevel * 0.5)
       @Query("SELECT COUNT(s) FROM InventoryStock s " +
              "WHERE s.warehouse.warehouseId = :warehouseId " +
              "AND s.availableQuantity <= (s.reorderLevel * 0.5) " +
              "AND s.availableQuantity > 0 " +
              "AND s.item.status = 'ACTIVE'")
       long countCriticalStockByWarehouse(
              @Param("warehouseId") Integer warehouseId);

       // Count normal stock by warehouse
       @Query("SELECT COUNT(s) FROM InventoryStock s " +
              "WHERE s.warehouse.warehouseId = :warehouseId " +
              "AND s.availableQuantity > s.reorderLevel " +
              "AND s.item.status = 'ACTIVE'")
       long countNormalStockByWarehouse(
              @Param("warehouseId") Integer warehouseId);


}