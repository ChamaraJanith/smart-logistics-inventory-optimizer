package com.optimizers.backend.service;

import java.util.List;
import com.optimizers.backend.dto.request.DeliveryItemRequestDTO;
import com.optimizers.backend.dto.response.DeliveryItemResponseDTO;

public interface DeliveryItemService {
    DeliveryItemResponseDTO addDeliveryItem(Integer deliveryId, DeliveryItemRequestDTO requestDTO);
    List<DeliveryItemResponseDTO> getDeliveryItems(Integer deliveryId);
    DeliveryItemResponseDTO updateDeliveryItem(Integer deliveryItemId, DeliveryItemRequestDTO requestDTO);
    void removeDeliveryItem(Integer deliveryItemId);
}
