package com.fooddelivery.inventory_service.controller;


import com.fooddelivery.inventory_service.dto.request.InventoryRequest;
import com.fooddelivery.inventory_service.dto.response.InventoryResponse;
import com.fooddelivery.inventory_service.dto.response.ResponseBuilder;
import com.fooddelivery.inventory_service.dto.response.StandardResponse;
import com.fooddelivery.inventory_service.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory APIs")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Create inventory")
    public ResponseEntity<StandardResponse<InventoryResponse>> createInventory(
            @Valid @RequestBody InventoryRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();
        InventoryResponse response = inventoryService.createInventory(ownerId, request);
        return ResponseBuilder.success(
                "Inventory created successfully",
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{menuItemId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Get inventory")
    public ResponseEntity<StandardResponse<InventoryResponse>> getInventory(
            @PathVariable UUID menuItemId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();
        InventoryResponse response = inventoryService.getInventory(ownerId, menuItemId);
        return ResponseBuilder.success(
                "Inventory fetched successfully",
                response,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{menuItemId}/stock")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @Operation(summary = "Update inventory stock")
    public ResponseEntity<StandardResponse<InventoryResponse>> updateStock(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();
        InventoryResponse response = inventoryService.updateStock(ownerId, menuItemId, quantity);
        return ResponseBuilder.success(
                "Inventory stock updated successfully",
                response,
                HttpStatus.OK
        );
    }

    @GetMapping("/{menuItemId}/availability")
    @Operation(summary = "Check inventory availability")
    public ResponseEntity<StandardResponse<Boolean>> checkAvailability(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity
    ) {
        boolean available = inventoryService.checkAvailability(menuItemId, quantity);
        return ResponseBuilder.success(
                "Inventory availability checked",
                available,
                HttpStatus.OK
        );
    }

    @PatchMapping("/internal/{menuItemId}/reduce")
    @Operation(summary = "Reduce inventory stock")
    public ResponseEntity<StandardResponse<InventoryResponse>> reduceStock(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity
    ) {
        InventoryResponse response = inventoryService.reduceStock(menuItemId, quantity);
        return ResponseBuilder.success(
                "Inventory stock reduced successfully",
                response,
                HttpStatus.OK
        );
    }
    @PatchMapping("/internal/{menuItemId}/increase")
    @Operation(summary = "Increase inventory stock")
    public ResponseEntity<StandardResponse<InventoryResponse>> increaseStock(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity
    ) {
        InventoryResponse response = inventoryService.increaseStock(menuItemId, quantity);
        return ResponseBuilder.success(
                "Inventory stock increased successfully",
                response,
                HttpStatus.OK
        );
    }
}
