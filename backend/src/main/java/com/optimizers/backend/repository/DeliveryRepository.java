package com.optimizers.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.optimizers.backend.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    long countByStatus(String status);

    long countByRequestedDateBetween(LocalDateTime start, LocalDateTime end);

    List<Delivery> findTop5ByOrderByCreatedAtDesc();

    List<Delivery> findByRequestedDateBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT EXTRACT(YEAR FROM created_at) AS year, " +
                   "EXTRACT(WEEK FROM created_at) AS week, " +
                   "COUNT(delivery_id) AS count " +
                   "FROM delivery " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY EXTRACT(YEAR FROM created_at), EXTRACT(WEEK FROM created_at) " +
                   "ORDER BY EXTRACT(YEAR FROM created_at) DESC, EXTRACT(WEEK FROM created_at) DESC",
           nativeQuery = true)
    List<Object[]> findWeeklyDeliveryCounts(@Param("startDate") LocalDateTime startDate);

    @Query(value = "SELECT EXTRACT(YEAR FROM created_at) AS year, " +
                   "EXTRACT(MONTH FROM created_at) AS month, " +
                   "COUNT(delivery_id) AS count " +
                   "FROM delivery " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY EXTRACT(YEAR FROM created_at), EXTRACT(MONTH FROM created_at) " +
                   "ORDER BY EXTRACT(YEAR FROM created_at) DESC, EXTRACT(MONTH FROM created_at) DESC",
           nativeQuery = true)
    List<Object[]> findMonthlyDeliveryCounts(@Param("startDate") LocalDateTime startDate);
}