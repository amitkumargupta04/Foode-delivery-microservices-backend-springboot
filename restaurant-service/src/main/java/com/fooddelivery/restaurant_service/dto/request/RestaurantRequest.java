package com.fooddelivery.restaurant_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Restaurant onboarding request")
public class RestaurantRequest {

    @NotBlank
    @Schema(
            description = "Restaurant name",
            example = "Food Corner"
    )
    private String name;

    @NotBlank
    @Schema(
            description = "Restaurant phone number",
            example = "9876543210"
    )
    private String phone;

    @Email
    @Schema(
            description = "Restaurant email address",
            example = "foodcorner@gmail.com"
    )
    private String email;

    @Schema(
            description = "Restaurant description",
            example = "North Indian and Chinese restaurant"
    )
    private String description;

    @Valid
    @Schema(description = "Restaurant address")
    private AddressRequest address;
}
