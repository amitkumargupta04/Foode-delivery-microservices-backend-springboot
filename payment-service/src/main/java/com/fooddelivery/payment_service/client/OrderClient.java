package com.fooddelivery.payment_service.client;

import com.fooddelivery.payment_service.dto.response.OrderResponse;
import com.fooddelivery.payment_service.dto.response.StandardResponse;
import com.fooddelivery.payment_service.enums.PaymentStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/v1/internal/orders/{orderId}")
    ResponseEntity<StandardResponse<OrderResponse>> getOrderById(
            @PathVariable UUID orderId
    );
    @PatchMapping("/api/v1/internal/orders/{orderId}/payment-status")
    ResponseEntity<StandardResponse<Void>> updatePaymentStatus(
            @PathVariable UUID orderId,
            @RequestParam PaymentStatus paymentStatus
    );
}
