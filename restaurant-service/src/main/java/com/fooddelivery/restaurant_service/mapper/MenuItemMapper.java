package com.fooddelivery.restaurant_service.mapper;

import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.entity.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItemResponse toResponse(MenuItem menuItem) {

        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .restaurantId(menuItem.getRestaurantId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .foodType(menuItem.getFoodType())
                .status(menuItem.getStatus())
                .imageUrl(menuItem.getImageUrl())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .build();
    }
}
