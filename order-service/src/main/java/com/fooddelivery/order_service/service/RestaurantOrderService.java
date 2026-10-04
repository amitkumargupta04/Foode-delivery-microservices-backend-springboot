package com.fooddelivery.order_service.service;


import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RestaurantOrderService {
    Page<OrderResponse> getRestaurantOrders(
            String ownerId,
            Pageable pageable
    );

    OrderResponse getRestaurantOrderById(
            String ownerId,
            UUID orderId
    );

    OrderResponse updateOrderStatus(
            String ownerId,
            UUID orderId,
            OrderStatus status
    );
}
