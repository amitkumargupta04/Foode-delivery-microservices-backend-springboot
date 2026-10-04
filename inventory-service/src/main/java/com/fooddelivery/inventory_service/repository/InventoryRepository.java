package com.fooddelivery.inventory_service.repository;

import com.fooddelivery.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findByMenuItemId(UUID menuItemId);

    Optional<Inventory> findByRestaurantIdAndMenuItemId(
            UUID restaurantId,
            UUID menuItemId
    );
    Optional<Inventory> findById(UUID id);

    List<Inventory> findByRestaurantId(UUID restaurantId);
}
