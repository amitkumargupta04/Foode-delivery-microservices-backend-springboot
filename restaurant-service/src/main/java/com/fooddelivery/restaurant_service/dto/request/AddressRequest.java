package com.fooddelivery.restaurant_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Restaurant address details")
public class AddressRequest {

    @Schema(
            description = "Complete address",
            example = "12 MG Road"
    )
    @NotBlank
    private String addressLine;

    @Schema(
            description = "Nearby landmark",
            example = "Near City Mall"
    )
    private String landmark;

    @Schema(
            description = "City",
            example = "Lucknow"
    )
    @NotBlank
    private String city;

    @Schema(
            description = "State",
            example = "Uttar Pradesh"
    )
    @NotBlank
    private String state;

    @Schema(
            description = "Postal code",
            example = "226001"
    )
    @NotBlank
    private String pincode;

    @Schema(
            description = "Latitude of restaurant location",
            example = "26.8467"
    )
    private Double latitude;

    @Schema(
            description = "Longitude of restaurant location",
            example = "80.9462"
    )
    private Double longitude;
}
