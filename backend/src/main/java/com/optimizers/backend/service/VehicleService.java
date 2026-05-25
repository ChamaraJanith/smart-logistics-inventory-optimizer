package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.VehicleRequestDTO;
import com.optimizers.backend.dto.response.VehicleResponseDTO;

public interface VehicleService {
    VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO);
    VehicleResponseDTO getVehicleById(Long id);
    List<VehicleResponseDTO> getAllVehicles();
    VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO requestDTO);
    void deleteVehicle(Long id);
}