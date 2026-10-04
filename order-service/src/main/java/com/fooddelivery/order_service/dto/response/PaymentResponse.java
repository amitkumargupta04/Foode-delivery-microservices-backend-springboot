package com.fooddelivery.order_service.dto.response;

import com.fooddelivery.order_service.enums.PaymentMethod;
import com.fooddelivery.order_service.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object containing payment transaction details")
public class PaymentResponse {

    @Schema(
            description = "Unique payment transaction ID",
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    private UUID id;

    @Schema(
            description = "Unique ID of the associated order",
            example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
    )
    private UUID orderId;

    @Schema(
            description = "ID of the user who made the payment",
            example = "usr_987654321"
    )
    private String userId;

    @Schema(
            description = "Total amount paid",
            example = "499.99"
    )
    private BigDecimal amount;

    @Schema(
            description = "Current state of the payment transaction",
            example = "SUCCESS"
    )
    private PaymentStatus status;

    @Schema(
            description = "Payment mode used for the transaction",
            example = "UPI"
    )
    private PaymentMethod paymentMethod;

    @Schema(
            description = "Timestamp when payment record was created",
            example = "2026-09-27T01:50:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Timestamp when payment record was last updated",
            example = "2026-09-27T01:50:05"
    )
    private LocalDateTime updatedAt;
}
