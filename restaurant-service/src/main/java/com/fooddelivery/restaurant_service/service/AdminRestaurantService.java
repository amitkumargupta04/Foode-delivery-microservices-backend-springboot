package com.fooddelivery.restaurant_service.service;

import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminRestaurantService {
    Page<RestaurantResponse> getAllRestaurants(
            RestaurantStatus status,
            String search,
            Pageable pageable
    );
    RestaurantResponse getRestaurantById(UUID restaurantId);
    RestaurantResponse updateRestaurantStatus(UUID restaurantId, RestaurantStatus status);
    Page<MenuItemResponse> getRestaurantMenuItems(
            UUID restaurantId,
            FoodType foodType,
            MenuItemStatus status,
            Pageable pageable
    );
    MenuItemResponse updateMenuItemStatus(
            UUID restaurantId,
            UUID menuItemId,
            MenuItemStatus status
    );
    void deleteRestaurant(UUID restaurantId);
}
