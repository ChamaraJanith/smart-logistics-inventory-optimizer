package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.request.WarehouseRequestDTO;
import com.optimizers.backend.dto.response.WarehouseResponseDTO;
import com.optimizers.backend.entity.Warehouse;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.WarehouseRepository;
import com.optimizers.backend.service.WarehouseService;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO requestDTO) {
        Warehouse warehouse = new Warehouse();
        mapToEntity(warehouse, requestDTO);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return mapToResponseDTO(savedWarehouse);
    }

    @Override
    public WarehouseResponseDTO getWarehouseById(Integer id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        return mapToResponseDTO(warehouse);
    }

    @Override
    public List<WarehouseResponseDTO> getAllWarehouses() {
        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseResponseDTO> getWarehousesByStatus(String status) {
        return warehouseRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseResponseDTO updateWarehouse(Integer id, WarehouseRequestDTO requestDTO) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        mapToEntity(warehouse, requestDTO);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return mapToResponseDTO(updatedWarehouse);
    }

    @Override
    public void deleteWarehouse(Integer id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        warehouseRepository.delete(warehouse);
    }

    private void mapToEntity(Warehouse warehouse, WarehouseRequestDTO requestDTO) {
        warehouse.setName(requestDTO.getName());
        warehouse.setAddress(requestDTO.getAddress());
        warehouse.setLatitude(requestDTO.getLatitude());
        warehouse.setLongitude(requestDTO.getLongitude());
        warehouse.setManagerName(requestDTO.getManagerName());
        warehouse.setContactNumber(requestDTO.getContactNumber());
        warehouse.setTotalCapacitySqm(requestDTO.getTotalCapacitySqm());
        warehouse.setStatus(requestDTO.getStatus());
    }

    private WarehouseResponseDTO mapToResponseDTO(Warehouse warehouse) {
        return new WarehouseResponseDTO(
                warehouse.getWarehouseId(),
                warehouse.getName(),
                warehouse.getAddress(),
                warehouse.getLatitude(),
                warehouse.getLongitude(),
                warehouse.getManagerName(),
                warehouse.getContactNumber(),
                warehouse.getTotalCapacitySqm(),
                warehouse.getStatus(),
                warehouse.getCreatedAt()
        );
    }
}