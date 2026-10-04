package com.fooddelivery.admin_service.controller;

import com.fooddelivery.admin_service.client.RestaurantClient;
import com.fooddelivery.admin_service.dto.response.MenuItemResponse;
import com.fooddelivery.admin_service.dto.response.RestaurantResponse;
import com.fooddelivery.admin_service.dto.response.StandardResponse;
import com.fooddelivery.admin_service.enums.FoodType;
import com.fooddelivery.admin_service.enums.MenuItemStatus;
import com.fooddelivery.admin_service.enums.RestaurantStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/restaurants")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Restaurant Management [Approved, Rejected, Deleted, Suspended]")
@SecurityRequirement(name = "bearerAuth")
public class AdminRestaurantController {

    private final RestaurantClient restaurantClient;

    @Operation(
            summary = "Get all restaurants",
            description = "Fetch restaurants with pagination, search and status filter"
    )
    @GetMapping
    public ResponseEntity<StandardResponse<List<RestaurantResponse>>> getAllRestaurants(

            @Parameter(description = "Filter by restaurant status")
            @RequestParam(required = false)
            RestaurantStatus status,

            @Parameter(description = "Search restaurant by name")
            @RequestParam(required = false)
            String search,

            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0")
            int page,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10")
            int size,

            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc")
            String sortDirection
    ) {

        return restaurantClient.getAllRestaurants(
                status,
                search,
                page,
                size,
                sortBy,
                sortDirection
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
        return restaurantClient.getRestaurantById(restaurantId);
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
        return restaurantClient.updateRestaurantStatus(restaurantId, status);
    }
    @Operation(
            summary = "Get restaurant menu items",
            description = "Fetch menu items of a specific restaurant with pagination and filters"
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
        return restaurantClient.getRestaurantMenuItems(
                restaurantId,
                foodType,
                status,
                page,
                size,
                sortBy,
                sortDirection
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
        return restaurantClient.updateMenuItemStatus(
                restaurantId,
                menuItemId,
                status
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
        return restaurantClient.deleteRestaurant(restaurantId);
    }
}
