package com.optimizers.backend.service;

import java.util.List;

import com.optimizers.backend.dto.request.DeliveryRequestDTO;
import com.optimizers.backend.dto.response.DeliveryResponseDTO;

public interface DeliveryService {
    DeliveryResponseDTO createDelivery(DeliveryRequestDTO requestDTO);
    DeliveryResponseDTO getDeliveryById(Integer id);
    List<DeliveryResponseDTO> getAllDeliveries();
    DeliveryResponseDTO updateDelivery(Integer id, DeliveryRequestDTO requestDTO);
    void deleteDelivery(Integer id);
}