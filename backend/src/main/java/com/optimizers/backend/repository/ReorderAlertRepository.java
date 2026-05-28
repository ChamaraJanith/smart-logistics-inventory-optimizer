// repository/ReorderAlertRepository.java
package com.optimizers.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.ReorderAlert;

public interface ReorderAlertRepository extends JpaRepository<ReorderAlert, Integer> {
    List<ReorderAlert> findByStatus(String status);
    List<ReorderAlert> findByWarehouse_WarehouseIdAndStatus(Integer warehouseId, String status);
    List<ReorderAlert> findBySeverityAndStatus(String severity, String status);
    // Check if open alert already exists for this stock
    Optional<ReorderAlert> findByStock_StockIdAndStatus(Integer stockId, String status);


    // Count open alerts
    long countByStatus(String status);

    // Count by severity and status
    long countBySeverityAndStatus(String severity, String status);

    // Count open alerts by warehouse
    long countByWarehouse_WarehouseIdAndStatus(
            Integer warehouseId, String status);

    // Recent 5 open alerts
    List<ReorderAlert> findTop5ByStatusOrderByTriggeredAtDesc(
            String status);
}