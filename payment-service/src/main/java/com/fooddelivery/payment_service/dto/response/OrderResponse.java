package com.fooddelivery.payment_service.dto.response;

import com.fooddelivery.payment_service.enums.OrderStatus;
import com.fooddelivery.payment_service.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response payload representing order details")
public class OrderResponse {

    @Schema(description = "Unique identifier of the order", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "User ID who placed the order", example = "usr_987654321")
    private String userId;

    @Schema(description = "Unique identifier of the restaurant", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
    private UUID restaurantId;

    @Schema(description = "List of ordered items with prices and quantities")
    private List<OrderItemResponse> items;

    @Schema(description = "Total bill amount for the order", example = "450.50")
    private BigDecimal totalAmount;

    @Schema(description = "Delivery address details")
    private DeliveryAddressResponse deliveryAddress;

    @Schema(description = "Current lifecycle status of the order", example = "PLACED")
    private OrderStatus status;

    @Schema(description = "Payment processing status", example = "PENDING")
    private PaymentStatus paymentStatus;

    @Schema(description = "Timestamp when the order was placed", example = "2026-09-23T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the order status was last updated", example = "2026-09-23T14:31:15")
    private LocalDateTime updatedAt;
}