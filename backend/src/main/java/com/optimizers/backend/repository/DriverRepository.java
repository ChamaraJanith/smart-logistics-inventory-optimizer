package com.optimizers.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimizers.backend.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}