package com.fooddelivery.restaurant_service.controller;


import com.fooddelivery.restaurant_service.dto.request.MenuItemRequest;
import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.dto.response.ResponseBuilder;
import com.fooddelivery.restaurant_service.dto.response.StandardResponse;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import com.fooddelivery.restaurant_service.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurant/menu-items")
@RequiredArgsConstructor
@Tag(name = "Restaurant Menu", description = "APIs for restaurant owners to manage menu items")
@SecurityRequirement(name = "bearerAuth")
public class MenuItemController {

    private final MenuItemService menuItemService;

    /**
     * Create a new menu item.
     */
    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(
            summary = "Create menu item",
            description = "Allows an approved restaurant owner to create a new menu item."
    )
    public ResponseEntity<StandardResponse<MenuItemResponse>> createMenuItem(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody MenuItemRequest request
    ) {

        String ownerId = jwt.getSubject();

        return ResponseBuilder.success(
                "Menu item created successfully",
                menuItemService.createMenuItem(ownerId, request),
                HttpStatus.CREATED
        );
    }

    /**
     * Get restaurant menu items with pagination and filters.
     */
    @GetMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(
            summary = "Get menu items",
            description = "Returns paginated menu items of the logged-in restaurant owner."
    )
    public ResponseEntity<StandardResponse<List<MenuItemResponse>>> getMenuItems(

            @AuthenticationPrincipal Jwt jwt,

            @Parameter(description = "Filter by food type", example = "VEG")
            @RequestParam(required = false)
            FoodType foodType,

            @Parameter(description = "Filter by menu item status", example = "AVAILABLE")
            @RequestParam(required = false)
            MenuItemStatus status,

            @Parameter(
                    description = "Page number (starts from 0)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            @Min(0)
            int page,

            @Parameter(
                    description = "Number of records per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            @Min(1)
            @Max(100)
            int size,

            @Parameter(
                    description = "Sort field",
                    example = "createdAt"
            )
            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @Parameter(
                    description = "Sort direction",
                    example = "desc"
            )
            @RequestParam(defaultValue = "desc")
            String sortDirection
    ) {

        String ownerId = jwt.getSubject();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.fromString(sortDirection),
                        sortBy
                )
        );

        Page<MenuItemResponse> data =
                menuItemService.getMenuItems(
                        ownerId,
                        foodType,
                        status,
                        pageable
                );

        return ResponseBuilder.successPaginated(
                "Menu items fetched successfully!",
                data
        );
    }

    /**
     * Get a menu item by ID.
     */
    @GetMapping("/{menuItemId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(
            summary = "Get menu item by ID",
            description = "Returns a specific menu item belonging to the logged-in restaurant."
    )
    public ResponseEntity<StandardResponse<MenuItemResponse>> getMenuItemById(

            @AuthenticationPrincipal Jwt jwt,

            @Parameter(
                    description = "Menu item ID",
                    required = true
            )
            @PathVariable UUID menuItemId
    ) {

        String ownerId = jwt.getSubject();

        return ResponseBuilder.success(
                "Menu item fetched successfully",
                menuItemService.getMenuItemById(ownerId, menuItemId),
                HttpStatus.OK
        );
    }

    /**
     * Update an existing menu item.
     */
    @PutMapping("/{menuItemId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(
            summary = "Update menu item",
            description = "Updates a menu item belonging to the logged-in restaurant owner."
    )
    public ResponseEntity<StandardResponse<MenuItemResponse>> updateMenuItem(

            @AuthenticationPrincipal Jwt jwt,

            @Parameter(
                    description = "Menu item ID",
                    required = true
            )
            @PathVariable UUID menuItemId,

            @Valid @RequestBody MenuItemRequest request
    ) {

        String ownerId = jwt.getSubject();

        return ResponseBuilder.success(
                "Menu item updated successfully",
                menuItemService.updateMenuItem(
                        ownerId,
                        menuItemId,
                        request
                ),
                HttpStatus.OK
        );
    }

    /**
     * Delete a menu item.
     */
    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(
            summary = "Delete menu item",
            description = "Deletes a menu item belonging to the logged-in restaurant owner."
    )
    public ResponseEntity<StandardResponse<Void>> deleteMenuItem(

            @AuthenticationPrincipal Jwt jwt,

            @Parameter(
                    description = "Menu item ID",
                    required = true
            )
            @PathVariable UUID menuItemId
    ) {

        String ownerId = jwt.getSubject();

        menuItemService.deleteMenuItem(ownerId, menuItemId);

        return ResponseBuilder.success(
                "Menu item deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}
