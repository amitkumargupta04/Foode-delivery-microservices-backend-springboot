package com.fooddelivery.admin_service.dto.response;

import com.fooddelivery.admin_service.enums.FoodType;
import com.fooddelivery.admin_service.enums.MenuItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Menu item response")
public class MenuItemResponse {

    @Schema(
            description = "Menu item unique identifier",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Restaurant ID",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID restaurantId;

    @Schema(
            description = "Menu item name",
            example = "Paneer Butter Masala"
    )
    private String name;

    @Schema(
            description = "Menu item description",
            example = "Creamy paneer curry cooked with Indian spices"
    )
    private String description;

    @Schema(
            description = "Menu item price",
            example = "249.99"
    )
    private BigDecimal price;

    @Schema(
            description = "Food type",
            example = "VEG"
    )
    private FoodType foodType;

    @Schema(
            description = "Menu item availability status",
            example = "AVAILABLE"
    )
    private MenuItemStatus status;

    @Schema(
            description = "Menu item image URL",
            example = "https://example.com/paneer.jpg"
    )
    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
