package com.fooddelivery.payment_service.dto.response;

import com.fooddelivery.payment_service.enums.PaymentMethod;
import com.fooddelivery.payment_service.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            description = "Razorpay order ID generated during checkout initialization",
            example = "order_N8aX1Z491Kls0A"
    )
    private String razorpayOrderId;

    @Schema(
            description = "Razorpay payment ID generated after successful transaction capture",
            example = "pay_N8aY2B382Pms1B"
    )
    private String razorpayPaymentId;

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
