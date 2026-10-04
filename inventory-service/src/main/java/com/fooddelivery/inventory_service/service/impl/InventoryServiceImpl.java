package com.fooddelivery.inventory_service.service.impl;

import com.fooddelivery.inventory_service.dto.request.InventoryRequest;
import com.fooddelivery.inventory_service.dto.response.InventoryResponse;
import com.fooddelivery.inventory_service.entity.Inventory;
import com.fooddelivery.inventory_service.exception.BadRequestException;
import com.fooddelivery.inventory_service.exception.ResourceNotFoundException;
import com.fooddelivery.inventory_service.mapper.InventoryMapper;
import com.fooddelivery.inventory_service.repository.InventoryRepository;
import com.fooddelivery.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse createInventory(String ownerId, InventoryRequest request) {
        if (inventoryRepository.findByRestaurantIdAndMenuItemId(request.getRestaurantId(), request.getMenuItemId()).isPresent()) {
            throw new BadRequestException("Inventory already exists for this menu item");
        }

        Inventory inventory = inventoryMapper.toEntity(request);
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        Inventory saved = inventoryRepository.save(inventory);

        return inventoryMapper.toResponse(saved);
    }

    @Override
    public InventoryResponse getInventory(String ownerId, UUID menuItemId) {
        Inventory inventory = inventoryRepository.findByMenuItemId(menuItemId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found for menu item: " + menuItemId)
        );
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateStock(
            String ownerId,
            UUID menuItemId,
            Integer quantity
    ) {
        if (quantity < 0) {
            throw new BadRequestException("Stock quantity cannot be negative");
        }
        Inventory inventory = inventoryRepository.findByMenuItemId(menuItemId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found for menu item: " + menuItemId)
        );

        inventory.setQuantity(quantity);
        inventory.setAvailable(quantity > 0);
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(saved);
    }

    @Override
    public boolean checkAvailability(UUID menuItemId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByMenuItemId(menuItemId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found for menu item: " + menuItemId)
        );
        return inventory.getAvailable() && inventory.getQuantity() >= quantity;
    }
    @Override
    @Transactional
    public InventoryResponse reduceStock(UUID menuItemId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByMenuItemId(menuItemId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found for menu item: " + menuItemId)
        );

        if (inventory.getQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock for menu item: " + menuItemId);
        }
        int remainingQuantity = inventory.getQuantity() - quantity;

        inventory.setQuantity(remainingQuantity);
        inventory.setAvailable(remainingQuantity > 0);
        inventory.setUpdatedAt(LocalDateTime.now());
        Inventory saved = inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(saved);
    }
    @Override
    @Transactional
    public InventoryResponse increaseStock(UUID menuItemId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByMenuItemId(menuItemId).orElseThrow(
                () -> new ResourceNotFoundException("Inventory not found for menu item: " + menuItemId)
        );
        int updatedQuantity = inventory.getQuantity() + quantity;
        inventory.setQuantity(updatedQuantity);
        inventory.setAvailable(updatedQuantity > 0);
        inventory.setUpdatedAt(LocalDateTime.now());
        Inventory saved = inventoryRepository.save(inventory);
        return inventoryMapper.toResponse(saved);
    }
}
