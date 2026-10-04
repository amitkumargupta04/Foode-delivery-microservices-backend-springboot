package com.fooddelivery.order_service.client;

import com.fooddelivery.order_service.dto.response.InventoryResponse;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/v1/inventory/{menuItemId}/availability")
    ResponseEntity<StandardResponse<Boolean>> checkAvailability(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity
    );

    @PatchMapping("/api/v1/inventory/internal/{menuItemId}/reduce")
    ResponseEntity<StandardResponse<InventoryResponse>> reduceStock(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity
    );
    @PatchMapping("/api/v1/inventory/internal/{menuItemId}/increase")
    ResponseEntity<StandardResponse<InventoryResponse>> increaseStock(
            @PathVariable UUID menuItemId,
            @RequestParam Integer quantity
    );
}
