package com.fooddelivery.restaurant_service.controller;


import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.dto.response.ResponseBuilder;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.dto.response.StandardResponse;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import com.fooddelivery.restaurant_service.service.AdminRestaurantService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant with Menu-Item public api's for listing")
public class RestaurantPublicController {

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
}
