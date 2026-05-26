package com.optimizers.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
}