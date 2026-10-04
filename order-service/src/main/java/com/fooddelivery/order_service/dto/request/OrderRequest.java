package com.fooddelivery.order_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for placing a new food order")
public class OrderRequest {

    @Schema(
            description = "Unique identifier of the restaurant",
            example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    private UUID restaurantId;

    @Schema(
            description = "List of food items to order",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    @Schema(
            description = "Delivery address details for the order",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Valid
    private DeliveryAddressRequest deliveryAddress;
}