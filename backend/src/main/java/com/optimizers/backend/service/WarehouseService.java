package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.WarehouseRequestDTO;
import com.optimizers.backend.dto.response.WarehouseResponseDTO;

public interface WarehouseService {
    WarehouseResponseDTO createWarehouse(WarehouseRequestDTO requestDTO);
    WarehouseResponseDTO getWarehouseById(Integer id);
    List<WarehouseResponseDTO> getAllWarehouses();
    List<WarehouseResponseDTO> getWarehousesByStatus(String status);
    WarehouseResponseDTO updateWarehouse(Integer id, WarehouseRequestDTO requestDTO);
    void deleteWarehouse(Integer id);
}