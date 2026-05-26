package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.VehicleRequestDTO;
import com.optimizers.backend.dto.response.VehicleResponseDTO;

public interface VehicleService {
    VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO);
    VehicleResponseDTO getVehicleById(Integer id);
    List<VehicleResponseDTO> getAllVehicles();
    VehicleResponseDTO updateVehicle(Integer id, VehicleRequestDTO requestDTO);
    void deleteVehicle(Integer id);
}