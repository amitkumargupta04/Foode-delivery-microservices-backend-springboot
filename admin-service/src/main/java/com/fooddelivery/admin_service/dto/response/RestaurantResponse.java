package com.fooddelivery.admin_service.dto.response;

import com.fooddelivery.admin_service.enums.RestaurantStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Restaurant response")
public class RestaurantResponse {

    @Schema(
            description = "Restaurant unique identifier",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Restaurant name",
            example = "Food Corner"
    )
    private String name;

    @Schema(
            description = "Restaurant owner's user ID",
            example = "user-123"
    )
    private String ownerId;

    @Schema(example = "9876543210")
    private String phone;

    @Schema(example = "foodcorner@gmail.com")
    private String email;

    @Schema(
            description = "Restaurant description",
            example = "North Indian and Chinese restaurant"
    )
    private String description;

    @Schema(
            description = "Restaurant approval status",
            example = "PENDING"
    )
    private RestaurantStatus status;

    @Schema(example = "true")
    private Boolean active;

    private AddressResponse address;
}
