package com.fooddelivery.inventory_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Inventory creation/update request")
public class InventoryRequest {

    @NotNull
    @Schema(
            description = "Restaurant ID",
            example = "669d72b4-12f1-44bd-87b8-c5b35ade1e40"
    )
    private UUID restaurantId;

    @NotNull
    @Schema(
            description = "Menu item ID",
            example = "747477ae-d778-477d-848b-b5a5a18d9645"
    )
    private UUID menuItemId;

    @NotNull
    @Min(0)
    @Schema(
            description = "Available stock quantity",
            example = "20"
    )
    private Integer quantity;
}