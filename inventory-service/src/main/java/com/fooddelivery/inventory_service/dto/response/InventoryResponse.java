package com.fooddelivery.inventory_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Inventory details")
public class InventoryResponse {

    @Schema(
            description = "Inventory ID",
            example = "a1b2c3d4-e5f6-4789-abcd-123456789abc"
    )
    private UUID id;

    @Schema(
            description = "Restaurant ID",
            example = "669d72b4-12f1-44bd-87b8-c5b35ade1e40"
    )
    private UUID restaurantId;

    @Schema(
            description = "Menu item ID",
            example = "747477ae-d778-477d-848b-b5a5a18d9645"
    )
    private UUID menuItemId;

    @Schema(
            description = "Available stock quantity",
            example = "18"
    )
    private Integer quantity;

    @Schema(
            description = "Whether the item is currently available",
            example = "true"
    )
    private Boolean available;

    @Schema(
            description = "Inventory creation timestamp",
            example = "2026-09-30T12:30:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Inventory last update timestamp",
            example = "2026-09-30T13:00:00"
    )
    private LocalDateTime updatedAt;
}
