package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.DriverRequestDTO;
import com.optimizers.backend.dto.response.DriverResponseDTO;

public interface DriverService {
    DriverResponseDTO createDriver(DriverRequestDTO requestDTO);
    DriverResponseDTO getDriverById(Integer id);
    List<DriverResponseDTO> getAllDrivers();
    DriverResponseDTO updateDriver(Integer id, DriverRequestDTO requestDTO);
    void deleteDriver(Integer id);
}