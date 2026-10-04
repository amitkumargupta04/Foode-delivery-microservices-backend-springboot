package com.fooddelivery.notification_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private Long userId;
    private String event;

    public OrderCreatedEvent() {
    }
}
