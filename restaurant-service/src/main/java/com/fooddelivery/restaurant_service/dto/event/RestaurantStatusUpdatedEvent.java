package com.fooddelivery.restaurant_service.dto.event;

import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantStatusUpdatedEvent {

    private String type;

    private UUID restaurantId;

    private String ownerId;

    private String restaurantName;

    private String ownerEmail;

    private RestaurantStatus status;

    private String rejectionReason;

    private LocalDateTime timestamp;
}
