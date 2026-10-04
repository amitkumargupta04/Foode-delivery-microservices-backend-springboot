package com.fooddelivery.inventory_service.service;

import com.fooddelivery.inventory_service.dto.request.InventoryRequest;
import com.fooddelivery.inventory_service.dto.response.InventoryResponse;

import java.util.UUID;

public interface InventoryService {
    InventoryResponse createInventory(
            String ownerId,
            InventoryRequest request
    );

    InventoryResponse getInventory(
            String ownerId,
            UUID menuItemId
    );

    InventoryResponse updateStock(
            String ownerId,
            UUID menuItemId,
            Integer quantity
    );

    boolean checkAvailability(
            UUID menuItemId,
            Integer quantity
    );
    InventoryResponse reduceStock(
            UUID menuItemId,
            Integer quantity
    );
    InventoryResponse increaseStock(
            UUID menuItemId,
            Integer quantity
    );
}
