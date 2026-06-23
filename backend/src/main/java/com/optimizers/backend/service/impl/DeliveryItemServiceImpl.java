package com.optimizers.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optimizers.backend.dto.request.DeliveryItemRequestDTO;
import com.optimizers.backend.dto.response.DeliveryItemResponseDTO;
import com.optimizers.backend.entity.Delivery;
import com.optimizers.backend.entity.DeliveryItem;
import com.optimizers.backend.entity.InventoryItem;
import com.optimizers.backend.exception.ResourceNotFoundException;
import com.optimizers.backend.repository.DeliveryItemRepository;
import com.optimizers.backend.repository.DeliveryRepository;
import com.optimizers.backend.repository.InventoryItemRepository;
import com.optimizers.backend.service.DeliveryItemService;

@Service
public class DeliveryItemServiceImpl implements DeliveryItemService {

    @Autowired
    private DeliveryItemRepository deliveryItemRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Override
    @Transactional
    public DeliveryItemResponseDTO addDeliveryItem(Integer deliveryId, DeliveryItemRequestDTO requestDTO) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + deliveryId));

        InventoryItem item = inventoryItemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + requestDTO.getItemId()));

        DeliveryItem deliveryItem = new DeliveryItem();
        deliveryItem.setDelivery(delivery);
        deliveryItem.setItem(item);
        deliveryItem.setQuantity(requestDTO.getQuantity());
        deliveryItem.setUnitPrice(requestDTO.getUnitPrice() != null ? requestDTO.getUnitPrice() : item.getUnitPrice());

        DeliveryItem saved = deliveryItemRepository.save(deliveryItem);
        updateDeliveryWeightAndVolume(delivery);

        return mapToResponseDTO(saved);
    }

    @Override
    public List<DeliveryItemResponseDTO> getDeliveryItems(Integer deliveryId) {
        return deliveryItemRepository.findByDelivery_DeliveryId(deliveryId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeliveryItemResponseDTO updateDeliveryItem(Integer deliveryItemId, DeliveryItemRequestDTO requestDTO) {
        DeliveryItem deliveryItem = deliveryItemRepository.findById(deliveryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery item not found: " + deliveryItemId));

        InventoryItem item = inventoryItemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + requestDTO.getItemId()));

        deliveryItem.setItem(item);
        deliveryItem.setQuantity(requestDTO.getQuantity());
        if (requestDTO.getUnitPrice() != null) {
            deliveryItem.setUnitPrice(requestDTO.getUnitPrice());
        }

        DeliveryItem saved = deliveryItemRepository.save(deliveryItem);
        updateDeliveryWeightAndVolume(deliveryItem.getDelivery());

        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public void removeDeliveryItem(Integer deliveryItemId) {
        DeliveryItem deliveryItem = deliveryItemRepository.findById(deliveryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery item not found: " + deliveryItemId));

        Delivery delivery = deliveryItem.getDelivery();
        deliveryItemRepository.delete(deliveryItem);
        updateDeliveryWeightAndVolume(delivery);
    }

    private void updateDeliveryWeightAndVolume(Delivery delivery) {
        List<DeliveryItem> items = deliveryItemRepository.findByDelivery_DeliveryId(delivery.getDeliveryId());
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalVolume = BigDecimal.ZERO;
        for (DeliveryItem item : items) {
            BigDecimal qty = item.getQuantity();
            BigDecimal weight = item.getItem().getUnitWeightKg();
            BigDecimal volume = item.getItem().getUnitVolume();
            if (weight != null) {
                totalWeight = totalWeight.add(weight.multiply(qty));
            }
            if (volume != null) {
                totalVolume = totalVolume.add(volume.multiply(qty));
            }
        }
        delivery.setPackageWeight(totalWeight);
        delivery.setPackageVolume(totalVolume);
        deliveryRepository.save(delivery);
    }

    private DeliveryItemResponseDTO mapToResponseDTO(DeliveryItem item) {
        return new DeliveryItemResponseDTO(
                item.getDeliveryItemId(),
                item.getDelivery().getDeliveryId(),
                item.getItem().getItemId(),
                item.getItem().getItemName(),
                item.getItem().getSku(),
                item.getQuantity(),
                item.getAllocatedStock() != null ? item.getAllocatedStock().getStockId() : null,
                item.getUnitPrice(),
                item.getTotalPrice() != null ? item.getTotalPrice() : item.getUnitPrice().multiply(item.getQuantity())
        );
    }
}
