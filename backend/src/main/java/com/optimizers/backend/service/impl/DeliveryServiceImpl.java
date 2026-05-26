package com.optimizers.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimizers.backend.dto.request.DeliveryRequestDTO;
import com.optimizers.backend.dto.response.DeliveryResponseDTO;
import com.optimizers.backend.entity.Delivery;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.DeliveryRepository;
import com.optimizers.backend.service.DeliveryService;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public DeliveryResponseDTO createDelivery(DeliveryRequestDTO requestDTO) {
        Delivery delivery = new Delivery();
        mapToEntity(delivery, requestDTO);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return mapToResponseDTO(savedDelivery);
    }

    @Override
    public DeliveryResponseDTO getDeliveryById(Integer id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        return mapToResponseDTO(delivery);
    }

    @Override
    public List<DeliveryResponseDTO> getAllDeliveries() {
        return deliveryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryResponseDTO updateDelivery(Integer id, DeliveryRequestDTO requestDTO) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));

        mapToEntity(delivery, requestDTO);
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return mapToResponseDTO(updatedDelivery);
    }

    @Override
    public void deleteDelivery(Integer id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        deliveryRepository.delete(delivery);
    }

    private void mapToEntity(Delivery delivery, DeliveryRequestDTO requestDTO) {
        delivery.setWarehouseId(requestDTO.getWarehouseId());
        delivery.setCustomerName(requestDTO.getCustomerName());
        delivery.setContactNumber(requestDTO.getContactNumber());
        delivery.setDeliveryAddress(requestDTO.getDeliveryAddress());
        delivery.setLatitude(requestDTO.getLatitude());
        delivery.setLongitude(requestDTO.getLongitude());
        delivery.setPackageWeight(requestDTO.getPackageWeight());
        delivery.setPackageVolume(requestDTO.getPackageVolume());
        delivery.setPriority(requestDTO.getPriority());
        delivery.setStatus(requestDTO.getStatus());
        delivery.setRequestedDate(requestDTO.getRequestedDate());
        delivery.setTimeWindowStart(requestDTO.getTimeWindowStart());
        delivery.setTimeWindowEnd(requestDTO.getTimeWindowEnd());
    }

    private DeliveryResponseDTO mapToResponseDTO(Delivery delivery) {
        return new DeliveryResponseDTO(
                delivery.getDeliveryId(),
                delivery.getWarehouseId(),
                delivery.getCustomerName(),
                delivery.getContactNumber(),
                delivery.getDeliveryAddress(),
                delivery.getLatitude(),
                delivery.getLongitude(),
                delivery.getPackageWeight(),
                delivery.getPackageVolume(),
                delivery.getPriority(),
                delivery.getStatus(),
                delivery.getRequestedDate(),
                delivery.getTimeWindowStart(),
                delivery.getTimeWindowEnd(),
                delivery.getCreatedAt()
        );
    }
}