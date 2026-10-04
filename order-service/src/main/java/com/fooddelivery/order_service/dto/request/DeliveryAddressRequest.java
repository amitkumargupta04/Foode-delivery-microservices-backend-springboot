package com.fooddelivery.order_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for adding or updating user delivery address")
public class DeliveryAddressRequest {

    @Schema(
            description = "Flat/House No., Building Name, Street Name",
            example = "Flat 402, Sunshine Apartments, MG Road",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String addressLine;

    @Schema(
            description = "Nearby identifiable location or landmark",
            example = "Near City Mall",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String landmark;

    @Schema(
            description = "City name",
            example = "Mumbai",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String city;

    @Schema(
            description = "State name",
            example = "Maharashtra",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String state;

    @Schema(
            description = "6-digit postal PIN code",
            example = "400001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String pincode;
}
