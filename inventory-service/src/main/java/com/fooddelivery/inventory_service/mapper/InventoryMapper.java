package com.fooddelivery.inventory_service.mapper;

import com.fooddelivery.inventory_service.dto.request.InventoryRequest;
import com.fooddelivery.inventory_service.dto.response.InventoryResponse;
import com.fooddelivery.inventory_service.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .restaurantId(inventory.getRestaurantId())
                .menuItemId(inventory.getMenuItemId())
                .quantity(inventory.getQuantity())
                .available(inventory.getAvailable())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }

    public Inventory toEntity(InventoryRequest request) {
        return Inventory.builder()
                .restaurantId(request.getRestaurantId())
                .menuItemId(request.getMenuItemId())
                .quantity(request.getQuantity())
                .available(request.getQuantity() > 0)
                .build();
    }
}
