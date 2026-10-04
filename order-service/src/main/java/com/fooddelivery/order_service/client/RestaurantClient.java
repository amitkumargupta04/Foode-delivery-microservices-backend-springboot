package com.fooddelivery.order_service.client;

import com.fooddelivery.order_service.dto.response.MenuItemResponse;
import com.fooddelivery.order_service.dto.response.RestaurantResponse;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import com.fooddelivery.order_service.enums.FoodType;
import com.fooddelivery.order_service.enums.MenuItemStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/api/v1/public/restaurants/{restaurantId}/menu-items")
    ResponseEntity<StandardResponse<List<MenuItemResponse>>> getRestaurantMenuItems(
            @PathVariable UUID restaurantId,
            @RequestParam(required = false) FoodType foodType,
            @RequestParam(required = false) MenuItemStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    );
    @GetMapping("/api/v1/internal/restaurants/owner/{ownerId}")
    ResponseEntity<StandardResponse<RestaurantResponse>> getRestaurantByOwnerId(
            @PathVariable String ownerId
    );
}
