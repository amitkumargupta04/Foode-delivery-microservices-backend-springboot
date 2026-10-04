package com.fooddelivery.restaurant_service.controller;

import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.dto.response.ResponseBuilder;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.dto.response.StandardResponse;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import com.fooddelivery.restaurant_service.service.AdminRestaurantService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Hidden
@RestController
@RequestMapping("/api/v1/admin/restaurants")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    @GetMapping
    public ResponseEntity<StandardResponse<List<RestaurantResponse>>> getAllRestaurants(
            @RequestParam(required = false) RestaurantStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<RestaurantResponse> restaurants =
                adminRestaurantService.getAllRestaurants(status, search, pageable);

        return ResponseBuilder.successPaginated(
                "Restaurants fetched successfully",
                restaurants
        );
    }
    @Operation(
            summary = "Get restaurant by ID",
            description = "Fetch a single restaurant by restaurant ID"
    )
    @GetMapping("/{restaurantId}")
    public ResponseEntity<StandardResponse<RestaurantResponse>> getRestaurantById(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId
    ) {

        RestaurantResponse restaurant = adminRestaurantService.getRestaurantById(restaurantId);
        return ResponseBuilder.success(
                "Restaurant fetched successfully",
                restaurant,
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Update restaurant status",
            description = "Approve, reject or suspend a restaurant"
    )
    @PatchMapping("/{restaurantId}/status")
    public ResponseEntity<StandardResponse<RestaurantResponse>> updateRestaurantStatus(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,
            @RequestParam RestaurantStatus status
    ) {
        RestaurantResponse restaurant = adminRestaurantService.updateRestaurantStatus(restaurantId, status);
        return ResponseBuilder.success(
                "Restaurant status updated successfully",
                restaurant,
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Get restaurant menu items",
            description = "Fetch menu items for a specific restaurant with pagination and filters"
    )
    @GetMapping("/{restaurantId}/menu-items")
    public ResponseEntity<StandardResponse<List<MenuItemResponse>>> getRestaurantMenuItems(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,

            @Parameter(description = "Filter by food type")
            @RequestParam(required = false) FoodType foodType,

            @Parameter(description = "Filter by menu item status")
            @RequestParam(required = false) MenuItemStatus status,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );
        Page<MenuItemResponse> menuItems =
                adminRestaurantService.getRestaurantMenuItems(
                        restaurantId,
                        foodType,
                        status,
                        pageable
                );
        return ResponseBuilder.successPaginated(
                "Restaurant menu items fetched successfully",
                menuItems
        );
    }
    @Operation(
            summary = "Update menu item status",
            description = "Update menu item status to AVAILABLE or UNAVAILABLE"
    )
    @PatchMapping("/{restaurantId}/menu-items/{menuItemId}/status")
    public ResponseEntity<StandardResponse<MenuItemResponse>> updateMenuItemStatus(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId,

            @Parameter(description = "Menu item ID", required = true)
            @PathVariable UUID menuItemId,

            @Parameter(description = "New menu item status", required = true)
            @RequestParam MenuItemStatus status
    ) {
        MenuItemResponse menuItem = adminRestaurantService.updateMenuItemStatus(restaurantId,menuItemId, status);
        return ResponseBuilder.success(
                "Menu item status updated successfully",
                menuItem,
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Delete restaurant",
            description = "Delete a restaurant by restaurant ID"
    )
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<StandardResponse<Void>> deleteRestaurant(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable UUID restaurantId
    ) {
        adminRestaurantService.deleteRestaurant(restaurantId);
        return ResponseBuilder.success(
                "Restaurant deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}
