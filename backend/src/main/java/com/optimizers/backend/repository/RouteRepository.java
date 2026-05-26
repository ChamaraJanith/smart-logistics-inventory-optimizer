package com.optimizers.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByRouteDate(LocalDate routeDate);
    List<Route> findByRouteStatus(String routeStatus);
}