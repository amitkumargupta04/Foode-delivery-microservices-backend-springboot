package com.fooddelivery.order_service.client;

import com.fooddelivery.order_service.dto.request.PaymentRequest;
import com.fooddelivery.order_service.dto.response.PaymentResponse;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/v1/payments")
    ResponseEntity<StandardResponse<PaymentResponse>> createPayment(
            @RequestBody PaymentRequest request
    );

    @GetMapping("/api/v1/payments/order/{orderId}")
    ResponseEntity<StandardResponse<PaymentResponse>> getPaymentByOrderId(
            @PathVariable UUID orderId
    );
}
