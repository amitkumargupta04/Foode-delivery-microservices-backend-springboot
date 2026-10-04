package com.fooddelivery.order_service.service.impl;

import com.fooddelivery.order_service.client.RestaurantClient;
import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.dto.response.RestaurantResponse;
import com.fooddelivery.order_service.entity.Order;
import com.fooddelivery.order_service.enums.OrderStatus;
import com.fooddelivery.order_service.exception.BadRequestException;
import com.fooddelivery.order_service.exception.ResourceNotFoundException;
import com.fooddelivery.order_service.mapper.OrderMapper;
import com.fooddelivery.order_service.repository.OrderRepository;
import com.fooddelivery.order_service.service.RestaurantOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantOrderServiceImpl implements RestaurantOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestaurantClient restaurantClient;

    @Override
    public Page<OrderResponse> getRestaurantOrders(String ownerId, Pageable pageable) {
        RestaurantResponse restaurant =
                restaurantClient.getRestaurantByOwnerId(ownerId)
                        .getBody()
                        .getData();
        UUID restaurantId = restaurant.getId();
        return orderRepository.findByRestaurantId(restaurantId, pageable).map(orderMapper::toResponse);
    }

    @Override
    public OrderResponse getRestaurantOrderById(String ownerId, UUID orderId) {
        RestaurantResponse restaurant = restaurantClient.getRestaurantByOwnerId(ownerId)
                        .getBody()
                        .getData();
        UUID restaurantId = restaurant.getId();
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new BadRequestException("Order does not belong to your restaurant");
        }
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String ownerId, UUID orderId, OrderStatus status) {
        RestaurantResponse restaurant = restaurantClient.getRestaurantByOwnerId(ownerId)
                        .getBody()
                        .getData();

        UUID restaurantId = restaurant.getId();
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new BadRequestException("Order does not belong to your restaurant");
        }
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }
}
