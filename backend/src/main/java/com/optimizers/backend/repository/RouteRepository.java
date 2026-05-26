package com.optimizers.backend.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.optimizers.backend.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    List<Route> findByRouteDate(LocalDate routeDate);

    List<Route> findByRouteStatus(String routeStatus);

    long countByRouteStatus(String routeStatus);

    long countByRouteDate(LocalDate routeDate);

    List<Route> findTop5ByOrderByCreatedAtDesc();

    @Query(value = "SELECT EXTRACT(YEAR FROM created_at) AS year, " +
                   "EXTRACT(WEEK FROM created_at) AS week, " +
                   "COUNT(route_id) AS count " +
                   "FROM route " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY EXTRACT(YEAR FROM created_at), EXTRACT(WEEK FROM created_at) " +
                   "ORDER BY EXTRACT(YEAR FROM created_at) DESC, EXTRACT(WEEK FROM created_at) DESC",
           nativeQuery = true)
    List<Object[]> findWeeklyRouteCounts(@Param("startDate") LocalDateTime startDate);

    @Query(value = "SELECT EXTRACT(YEAR FROM created_at) AS year, " +
                   "EXTRACT(MONTH FROM created_at) AS month, " +
                   "COUNT(route_id) AS count " +
                   "FROM route " +
                   "WHERE created_at >= :startDate " +
                   "GROUP BY EXTRACT(YEAR FROM created_at), EXTRACT(MONTH FROM created_at) " +
                   "ORDER BY EXTRACT(YEAR FROM created_at) DESC, EXTRACT(MONTH FROM created_at) DESC",
           nativeQuery = true)
    List<Object[]> findMonthlyRouteCounts(@Param("startDate") LocalDateTime startDate);
}