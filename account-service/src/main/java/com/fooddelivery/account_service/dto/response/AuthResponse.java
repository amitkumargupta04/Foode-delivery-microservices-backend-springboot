package com.fooddelivery.account_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Authentication response containing JWT token details")
public class AuthResponse {

    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Token Type", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration duration in milliseconds", example = "86400000")
    private long expiresIn;
}
