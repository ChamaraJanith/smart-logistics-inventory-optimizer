// repository/StockTransactionRepository.java
package com.optimizers.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

import com.optimizers.backend.entity.StockTransaction;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Integer> {
    List<StockTransaction> findByItem_ItemIdOrderByCreatedAtDesc(Integer itemId);
    List<StockTransaction> findByWarehouse_WarehouseIdOrderByCreatedAtDesc(Integer warehouseId);
    List<StockTransaction> findByTransactionTypeOrderByCreatedAtDesc(String transactionType);

        // Count today's transactions
    @Query("SELECT COUNT(t) FROM StockTransaction t " +
        "WHERE t.createdAt >= :startOfDay " +
        "AND t.createdAt <= :endOfDay")
    long countTodayTransactions(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}