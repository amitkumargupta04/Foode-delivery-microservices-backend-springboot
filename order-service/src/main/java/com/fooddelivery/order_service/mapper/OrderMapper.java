package com.fooddelivery.order_service.mapper;

import com.fooddelivery.order_service.dto.response.DeliveryAddressResponse;
import com.fooddelivery.order_service.dto.response.OrderItemResponse;
import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.entity.DeliveryAddress;
import com.fooddelivery.order_service.entity.Order;
import com.fooddelivery.order_service.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .restaurantId(order.getRestaurantId())
                .items(items)
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(toAddressResponse(order.getDeliveryAddress()))
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return OrderItemResponse.builder()
                .menuItemId(item.getMenuItemId())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }

    private DeliveryAddressResponse toAddressResponse(
            DeliveryAddress address
    ) {
        if (address == null) {
            return null;
        }

        return DeliveryAddressResponse.builder()
                .addressLine(address.getAddressLine())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .build();
    }
}
