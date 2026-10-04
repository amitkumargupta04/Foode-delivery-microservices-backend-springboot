package com.fooddelivery.order_service.dto.request;

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
@Schema(description = "Request payload for an individual order item")
public class OrderItemRequest {

    @Schema(
            description = "Unique identifier of the menu item",
            example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private UUID menuItemId;

    @Schema(
            description = "Quantity of the menu item (minimum 1)",
            example = "2",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Min(1)
    private Integer quantity;
}
