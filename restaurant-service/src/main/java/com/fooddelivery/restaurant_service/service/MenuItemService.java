package com.fooddelivery.restaurant_service.service;

import com.fooddelivery.restaurant_service.dto.request.MenuItemRequest;
import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemService {

    MenuItemResponse createMenuItem(
            String ownerId,
            MenuItemRequest request
    );

    Page<MenuItemResponse> getMenuItems(
            String ownerId,
            FoodType foodType,
            MenuItemStatus status,
            Pageable pageable
    );

    MenuItemResponse getMenuItemById(
            String ownerId,
            UUID menuItemId
    );

    MenuItemResponse updateMenuItem(
            String ownerId,
            UUID menuItemId,
            MenuItemRequest request
    );

    void deleteMenuItem(
            String ownerId,
            UUID menuItemId
    );
}
