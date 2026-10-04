package com.fooddelivery.admin_service.client;

import com.fooddelivery.admin_service.config.FeignClientConfig;
import com.fooddelivery.admin_service.dto.response.MenuItemResponse;
import com.fooddelivery.admin_service.dto.response.RestaurantResponse;
import com.fooddelivery.admin_service.dto.response.StandardResponse;
import com.fooddelivery.admin_service.enums.FoodType;
import com.fooddelivery.admin_service.enums.MenuItemStatus;
import com.fooddelivery.admin_service.enums.RestaurantStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "restaurant-service", configuration = FeignClientConfig.class)
public interface RestaurantClient {

    @GetMapping("/api/v1/admin/restaurants")
    ResponseEntity<StandardResponse<List<RestaurantResponse>>> getAllRestaurants(
            @RequestParam(required = false) RestaurantStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    );

    @GetMapping("/api/v1/admin/restaurants/{restaurantId}")
    ResponseEntity<StandardResponse<RestaurantResponse>> getRestaurantById(@PathVariable UUID restaurantId);

    @PatchMapping("/api/v1/admin/restaurants/{restaurantId}/status")
    ResponseEntity<StandardResponse<RestaurantResponse>> updateRestaurantStatus(
            @PathVariable UUID restaurantId,
            @RequestParam RestaurantStatus status
    );

    @GetMapping("/api/v1/admin/restaurants/{restaurantId}/menu-items")
    ResponseEntity<StandardResponse<List<MenuItemResponse>>> getRestaurantMenuItems(
            @PathVariable UUID restaurantId,
            @RequestParam(required = false) FoodType foodType,
            @RequestParam(required = false) MenuItemStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    );
    @PatchMapping("/api/v1/admin/restaurants/{restaurantId}/menu-items/{menuItemId}/status")
    ResponseEntity<StandardResponse<MenuItemResponse>> updateMenuItemStatus(
            @PathVariable UUID restaurantId,
            @PathVariable UUID menuItemId,
            @RequestParam MenuItemStatus status
    );
    @DeleteMapping("/api/v1/admin/restaurants/{restaurantId}")
    ResponseEntity<StandardResponse<Void>> deleteRestaurant(
            @PathVariable UUID restaurantId
    );
}
