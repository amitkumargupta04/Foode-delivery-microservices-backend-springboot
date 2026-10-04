package com.fooddelivery.payment_service.service;

import com.fooddelivery.payment_service.dto.request.PaymentRequest;
import com.fooddelivery.payment_service.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse createPayment(
            String userId,
            PaymentRequest request
    );

    PaymentResponse getPaymentById(
            UUID paymentId
    );

    PaymentResponse getPaymentByOrderId(
            UUID orderId
    );
}
