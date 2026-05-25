package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.DriverRequestDTO;
import com.optimizers.backend.dto.response.DriverResponseDTO;

public interface DriverService {
    DriverResponseDTO createDriver(DriverRequestDTO requestDTO);
    DriverResponseDTO getDriverById(Long id);
    List<DriverResponseDTO> getAllDrivers();
    DriverResponseDTO updateDriver(Long id, DriverRequestDTO requestDTO);
    void deleteDriver(Long id);
}