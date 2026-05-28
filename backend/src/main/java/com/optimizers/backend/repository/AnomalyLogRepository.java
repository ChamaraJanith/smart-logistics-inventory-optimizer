// repository/AnomalyLogRepository.java
package com.optimizers.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.AnomalyLog;

public interface AnomalyLogRepository
        extends JpaRepository<AnomalyLog, Integer> {

    List<AnomalyLog> findByStatus(String status);

    List<AnomalyLog> findByItem_ItemIdOrderByDetectedAtDesc(
            Integer itemId);

    List<AnomalyLog> findByWarehouse_WarehouseIdOrderByDetectedAtDesc(
            Integer warehouseId);

    List<AnomalyLog> findByAnomalyTypeAndStatus(
            String anomalyType, String status);

        // Count open anomalies
        long countByStatus(String status);

        // Recent 5 open anomalies
        List<AnomalyLog> findTop5ByStatusOrderByDetectedAtDesc(
                String status);
}