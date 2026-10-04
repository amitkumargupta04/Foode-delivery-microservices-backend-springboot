package com.fooddelivery.order_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response payload representing delivery address details")
public class DeliveryAddressResponse {

    @Schema(
            description = "Flat/House No., Building Name, Street Name",
            example = "Flat 402, Sunshine Apartments, MG Road"
    )
    private String addressLine;

    @Schema(
            description = "Nearby identifiable location or landmark",
            example = "Near City Mall"
    )
    private String landmark;

    @Schema(
            description = "City name",
            example = "Mumbai"
    )
    private String city;

    @Schema(
            description = "State name",
            example = "Maharashtra"
    )
    private String state;

    @Schema(
            description = "6-digit postal PIN code",
            example = "400001"
    )
    private String pincode;
}
