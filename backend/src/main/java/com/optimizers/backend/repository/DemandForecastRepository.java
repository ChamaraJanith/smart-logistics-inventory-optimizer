// repository/DemandForecastRepository.java
package com.optimizers.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.optimizers.backend.entity.DemandForecast;

public interface DemandForecastRepository
        extends JpaRepository<DemandForecast, Integer> {

    List<DemandForecast> findByItem_ItemIdOrderByForecastDateAsc(Integer itemId);

    List<DemandForecast> findByWarehouse_WarehouseIdOrderByForecastDateAsc(
            Integer warehouseId);

    Optional<DemandForecast> findByItem_ItemIdAndWarehouse_WarehouseIdAndForecastDateAndModelVersion(
            Integer itemId, Integer warehouseId,
            LocalDate forecastDate, String modelVersion);

    // Next 7 days forecasts for an item
    @Query("SELECT f FROM DemandForecast f " +
           "WHERE f.item.itemId = :itemId " +
           "AND f.warehouse.warehouseId = :warehouseId " +
           "AND f.forecastDate BETWEEN :startDate AND :endDate " +
           "ORDER BY f.forecastDate ASC")
    List<DemandForecast> findForecastsInRange(
            @Param("itemId") Integer itemId,
            @Param("warehouseId") Integer warehouseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}