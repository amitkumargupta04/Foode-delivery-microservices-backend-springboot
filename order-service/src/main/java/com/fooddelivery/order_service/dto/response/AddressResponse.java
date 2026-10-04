package com.fooddelivery.order_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Restaurant address response")
public class AddressResponse {

    @Schema(
            description = "Address unique identifier",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Complete address",
            example = "12 MG Road"
    )
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
    private String city;

    @Schema(
            description = "State",
            example = "Uttar Pradesh"
    )
    private String state;

    @Schema(
            description = "Postal code",
            example = "226001"
    )
    private String pincode;

    @Schema(
            description = "Latitude of location",
            example = "26.8467"
    )
    private Double latitude;

    @Schema(
            description = "Longitude of location",
            example = "80.9462"
    )
    private Double longitude;
}
