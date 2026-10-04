package com.fooddelivery.order_service.dto.request;

import com.fooddelivery.order_service.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for initiating a payment")
public class PaymentRequest {

    @Schema(
            description = "Unique identifier of the order to be paid",
            example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Order ID cannot be null")
    private UUID orderId;

    @Schema(
            description = "Method of payment (e.g., CREDIT_CARD, UPI, PAYPAL, CASH_ON_DELIVERY)",
            example = "UPI",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;
}
