// repository/StockTransactionRepository.java
package com.optimizers.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.StockTransaction;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Integer> {
    List<StockTransaction> findByItem_ItemIdOrderByCreatedAtDesc(Integer itemId);
    List<StockTransaction> findByWarehouse_WarehouseIdOrderByCreatedAtDesc(Integer warehouseId);
    List<StockTransaction> findByTransactionTypeOrderByCreatedAtDesc(String transactionType);
}