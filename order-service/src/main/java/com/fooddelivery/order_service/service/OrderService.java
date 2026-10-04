package com.fooddelivery.order_service.service;

import com.fooddelivery.order_service.dto.request.OrderRequest;
import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.dto.response.PaymentResponse;
import com.fooddelivery.order_service.enums.PaymentMethod;
import com.fooddelivery.order_service.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(
            String userId,
            OrderRequest request
    );

    Page<OrderResponse> getMyOrders(
            String userId,
            Pageable pageable
    );

    OrderResponse getMyOrderById(
            String userId,
            UUID orderId
    );

    void cancelOrder(
            String userId,
            UUID orderId
    );

    PaymentResponse createPayment(
            String userId,
            UUID orderId,
            PaymentMethod paymentMethod
    );

    PaymentResponse getPaymentByOrderId(
            String userId,
            UUID orderId
    );
    OrderResponse getOrderById(UUID orderId);
    void updatePaymentStatus(UUID orderId, PaymentStatus paymentStatus);
}
