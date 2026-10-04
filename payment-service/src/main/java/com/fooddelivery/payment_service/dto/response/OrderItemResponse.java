package com.fooddelivery.payment_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response payload representing an item within an order")
public class OrderItemResponse {

    @Schema(description = "Unique identifier of the menu item", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    private UUID menuItemId;

    @Schema(description = "Name of the menu item", example = "Paneer Butter Masala")
    private String name;

    @Schema(description = "Price per unit of the item", example = "220.00")
    private BigDecimal price;

    @Schema(description = "Quantity ordered", example = "2")
    private Integer quantity;

    @Schema(description = "Subtotal for this item (price * quantity)", example = "440.00")
    private BigDecimal subtotal;
}
