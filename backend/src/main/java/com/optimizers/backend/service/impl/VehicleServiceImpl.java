package com.optimizers.backend.service.impl;

import com.optimizers.backend.dto.request.VehicleRequestDTO;
import com.optimizers.backend.dto.response.VehicleResponseDTO;
import com.optimizers.backend.entity.Vehicle;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.VehicleRepository;
import com.optimizers.backend.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(requestDTO.getVehicleNumber());
        vehicle.setVehicleType(requestDTO.getVehicleType());
        vehicle.setCapacityKg(requestDTO.getCapacityKg());
        vehicle.setMaxVolume(requestDTO.getMaxVolume());
        vehicle.setFuelType(requestDTO.getFuelType());
        vehicle.setCurrentStatus(requestDTO.getCurrentStatus());
        vehicle.setCurrentLatitude(requestDTO.getCurrentLatitude());
        vehicle.setCurrentLongitude(requestDTO.getCurrentLongitude());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(savedVehicle);
    }

    @Override
    public VehicleResponseDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return mapToResponseDTO(vehicle);
    }

    @Override
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO requestDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setVehicleNumber(requestDTO.getVehicleNumber());
        vehicle.setVehicleType(requestDTO.getVehicleType());
        vehicle.setCapacityKg(requestDTO.getCapacityKg());
        vehicle.setMaxVolume(requestDTO.getMaxVolume());
        vehicle.setFuelType(requestDTO.getFuelType());
        vehicle.setCurrentStatus(requestDTO.getCurrentStatus());
        vehicle.setCurrentLatitude(requestDTO.getCurrentLatitude());
        vehicle.setCurrentLongitude(requestDTO.getCurrentLongitude());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToResponseDTO(updatedVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicleRepository.delete(vehicle);
    }

    private VehicleResponseDTO mapToResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getVehicleId(),
                vehicle.getVehicleNumber(),
                vehicle.getVehicleType(),
                vehicle.getCapacityKg(),
                vehicle.getMaxVolume(),
                vehicle.getFuelType(),
                vehicle.getCurrentStatus(),
                vehicle.getCurrentLatitude(),
                vehicle.getCurrentLongitude(),
                vehicle.getCreatedAt()
        );
    }
}