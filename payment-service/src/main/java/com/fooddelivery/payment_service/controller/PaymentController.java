package com.fooddelivery.payment_service.controller;

import com.fooddelivery.payment_service.dto.request.PaymentRequest;
import com.fooddelivery.payment_service.dto.response.PaymentResponse;
import com.fooddelivery.payment_service.dto.response.ResponseBuilder;
import com.fooddelivery.payment_service.dto.response.StandardResponse;
import com.fooddelivery.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(
        name = "Payment APIs",
        description = "Customer payment management APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        PaymentResponse payment = paymentService.createPayment(userId, request);
        return ResponseBuilder.success(
                "Payment created successfully",
                payment,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<PaymentResponse>> getPaymentById(
            @PathVariable UUID paymentId
    ) {

        PaymentResponse payment =
                paymentService.getPaymentById(paymentId);

        return ResponseBuilder.success(
                "Payment fetched successfully",
                payment,
                HttpStatus.OK
        );
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Hidden
    public ResponseEntity<StandardResponse<PaymentResponse>> getPaymentByOrderId(
            @PathVariable UUID orderId
    ) {
        PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseBuilder.success(
                "Payment fetched successfully",
                payment,
                HttpStatus.OK
        );
    }
}
