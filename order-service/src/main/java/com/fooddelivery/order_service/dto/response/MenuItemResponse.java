package com.fooddelivery.order_service.dto.response;

import com.fooddelivery.order_service.enums.FoodType;
import com.fooddelivery.order_service.enums.MenuItemStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponse {

    private UUID id;
    private UUID restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private FoodType foodType;
    private MenuItemStatus status;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
