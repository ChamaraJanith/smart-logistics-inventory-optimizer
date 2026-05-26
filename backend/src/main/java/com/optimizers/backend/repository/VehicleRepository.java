package com.optimizers.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}