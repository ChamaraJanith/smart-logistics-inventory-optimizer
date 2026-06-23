package com.optimizers.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.optimizers.backend.entity.DeliveryItem;

@Repository
public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, Integer> {
    List<DeliveryItem> findByDelivery_DeliveryId(Integer deliveryId);
}
