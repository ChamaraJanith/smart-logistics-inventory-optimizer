package com.optimizers.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    List<Warehouse> findByStatus(String status);
}