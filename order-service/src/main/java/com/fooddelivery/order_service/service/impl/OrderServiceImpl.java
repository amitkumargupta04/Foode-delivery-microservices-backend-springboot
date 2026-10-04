package com.fooddelivery.order_service.service.impl;

import com.fooddelivery.order_service.client.InventoryClient;
import com.fooddelivery.order_service.client.PaymentClient;
import com.fooddelivery.order_service.client.RestaurantClient;
import com.fooddelivery.order_service.dto.request.OrderItemRequest;
import com.fooddelivery.order_service.dto.request.OrderRequest;
import com.fooddelivery.order_service.dto.request.PaymentRequest;
import com.fooddelivery.order_service.dto.response.MenuItemResponse;
import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.dto.response.PaymentResponse;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import com.fooddelivery.order_service.entity.DeliveryAddress;
import com.fooddelivery.order_service.entity.Order;
import com.fooddelivery.order_service.entity.OrderItem;
import com.fooddelivery.order_service.enums.MenuItemStatus;
import com.fooddelivery.order_service.enums.OrderStatus;
import com.fooddelivery.order_service.enums.PaymentMethod;
import com.fooddelivery.order_service.enums.PaymentStatus;
import com.fooddelivery.order_service.exception.BadRequestException;
import com.fooddelivery.order_service.exception.ResourceNotFoundException;
import com.fooddelivery.order_service.mapper.OrderMapper;
import com.fooddelivery.order_service.repository.OrderRepository;
import com.fooddelivery.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestaurantClient restaurantClient;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public OrderResponse createOrder(String userId, OrderRequest request) {
        // 1. Restaurant/menu items validate
        ResponseEntity<StandardResponse<List<MenuItemResponse>>> menuResponse =
                restaurantClient.getRestaurantMenuItems(
                        request.getRestaurantId(),
                        null,
                        MenuItemStatus.AVAILABLE,
                        0,
                        100,
                        "createdAt",
                        "asc"
                );

        List<MenuItemResponse> menuItems = menuResponse.getBody().getData();
        // 2. Check inventory for ALL items first
        for (OrderItemRequest item : request.getItems()) {
            ResponseEntity<StandardResponse<Boolean>> response = inventoryClient.checkAvailability(item.getMenuItemId(), item.getQuantity());
            Boolean available = response.getBody().getData();
            if (!Boolean.TRUE.equals(available)) {
                throw new BadRequestException("Insufficient stock for menu item: " + item.getMenuItemId()
                );
            }
        }

        // 3. Build order items
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItemResponse menuItem = menuItems.stream()
                    .filter(item -> item.getId().equals(itemRequest.getMenuItemId()))
                    .findFirst().orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + itemRequest.getMenuItemId()));

            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            OrderItem orderItem = OrderItem.builder()
                    .menuItemId(menuItem.getId())
                    .name(menuItem.getName())
                    .price(menuItem.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build();
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(itemTotal);
        }

        // 4. Create order
        Order order = Order.builder()
                .userId(userId)
                .restaurantId(request.getRestaurantId())
                .items(orderItems)
                .totalAmount(totalAmount)
                .deliveryAddress(
                        DeliveryAddress.builder()
                                .addressLine(
                                        request.getDeliveryAddress()
                                                .getAddressLine())
                                .landmark(
                                        request.getDeliveryAddress()
                                                .getLandmark())
                                .city(
                                        request.getDeliveryAddress()
                                                .getCity())
                                .state(
                                        request.getDeliveryAddress()
                                                .getState())
                                .pincode(
                                        request.getDeliveryAddress()
                                                .getPincode())
                                .build()
                )
                .status(OrderStatus.PLACED)
                .paymentStatus(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // 5. Reduce stock AFTER order is successfully saved
        for (OrderItemRequest item : request.getItems()) {

            inventoryClient.reduceStock(
                    item.getMenuItemId(),
                    item.getQuantity()
            );
        }

        return orderMapper.toResponse(savedOrder);
    }
    @Override
    public Page<OrderResponse> getMyOrders(String userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(orderMapper::toResponse);
    }
    @Override
    public OrderResponse getMyOrderById(
            String userId,
            UUID orderId
    ) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        return orderMapper.toResponse(order);
    }
    @Override
    @Transactional
    public void cancelOrder(String userId, UUID orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BadRequestException("Order cannot be cancelled at this stage");
        }
        for (OrderItem item : order.getItems()) {
            inventoryClient.increaseStock(item.getMenuItemId(), item.getQuantity());
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public PaymentResponse createPayment(String userId, UUID orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Payment cannot be created for cancelled order");
        }
        PaymentRequest request = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMethod(paymentMethod)
                .build();

        ResponseEntity<StandardResponse<PaymentResponse>> response = paymentClient.createPayment(request);
        return response.getBody().getData();
    }
    @Override
    public PaymentResponse getPaymentByOrderId(String userId, UUID orderId) {
        orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        ResponseEntity<StandardResponse<PaymentResponse>> response = paymentClient.getPaymentByOrderId(orderId);
        return response.getBody().getData();
    }
    @Override
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        return orderMapper.toResponse(order);
    }

    @Transactional
    @Override
    public void updatePaymentStatus(UUID orderId, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with id: " + orderId)
        );
        order.setPaymentStatus(paymentStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
