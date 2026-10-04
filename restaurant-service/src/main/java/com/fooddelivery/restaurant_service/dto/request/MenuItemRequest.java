package com.fooddelivery.restaurant_service.dto.request;

import com.fooddelivery.restaurant_service.enums.FoodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to create or update a menu item")
public class MenuItemRequest {

    @NotNull
    @Schema(
            description = "Restaurant ID",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID restaurantId;

    @NotBlank
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

    @NotNull
    @DecimalMin(value = "0.01")
    @Schema(
            description = "Menu item price",
            example = "249.99"
    )
    private BigDecimal price;

    @NotNull
    @Schema(
            description = "Food type",
            example = "VEG"
    )
    private FoodType foodType;

    @Schema(
            description = "Menu item image URL",
            example = "https://example.com/paneer.jpg"
    )
    private String imageUrl;
}
