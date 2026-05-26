package com.optimizers.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.RouteDelivery;

public interface RouteDeliveryRepository extends JpaRepository<RouteDelivery, Integer> {
    List<RouteDelivery> findByRouteRouteId(Integer routeId);
    List<RouteDelivery> findByDeliveryDeliveryId(Integer deliveryId);
    Optional<RouteDelivery> findByRouteRouteIdAndDeliveryDeliveryId(Integer routeId, Integer deliveryId);
    boolean existsByRouteRouteIdAndDeliveryDeliveryId(Integer routeId, Integer deliveryId);
    boolean existsByRouteRouteIdAndStopSequence(Integer routeId, Integer stopSequence);
}