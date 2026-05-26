package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.request.DriverRequestDTO;
import com.optimizers.backend.dto.response.DriverResponseDTO;
import com.optimizers.backend.entity.Driver;
import com.optimizers.backend.entity.Vehicle;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.DriverRepository;
import com.optimizers.backend.repository.VehicleRepository;
import com.optimizers.backend.service.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public DriverResponseDTO createDriver(DriverRequestDTO requestDTO) {
        Driver driver = new Driver();
        driver.setDriverName(requestDTO.getDriverName());
        driver.setPhone(requestDTO.getPhone());
        driver.setLicenseNo(requestDTO.getLicenseNo());
        driver.setStatus(requestDTO.getStatus());

        if (requestDTO.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(requestDTO.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + requestDTO.getVehicleId()));
            driver.setVehicle(vehicle);
        }

        Driver savedDriver = driverRepository.save(driver);
        return mapToResponseDTO(savedDriver);
    }

    @Override
    public DriverResponseDTO getDriverById(Integer id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        return mapToResponseDTO(driver);
    }

    @Override
    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponseDTO updateDriver(Integer id, DriverRequestDTO requestDTO) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));

        driver.setDriverName(requestDTO.getDriverName());
        driver.setPhone(requestDTO.getPhone());
        driver.setLicenseNo(requestDTO.getLicenseNo());
        driver.setStatus(requestDTO.getStatus());

        if (requestDTO.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(requestDTO.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + requestDTO.getVehicleId()));
            driver.setVehicle(vehicle);
        } else {
            driver.setVehicle(null);
        }

        Driver updatedDriver = driverRepository.save(driver);
        return mapToResponseDTO(updatedDriver);
    }

    @Override
    public void deleteDriver(Integer id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
        driverRepository.delete(driver);
    }

    private DriverResponseDTO mapToResponseDTO(Driver driver) {
        Integer vehicleId = null;
        String vehicleNumber = null;

        if (driver.getVehicle() != null) {
            vehicleId = driver.getVehicle().getVehicleId();
            vehicleNumber = driver.getVehicle().getVehicleNumber();
        }

        return new DriverResponseDTO(
                driver.getDriverId(),
                driver.getDriverName(),
                driver.getPhone(),
                driver.getLicenseNo(),
                driver.getStatus(),
                vehicleId,
                vehicleNumber,
                driver.getCreatedAt()
        );
    }
}