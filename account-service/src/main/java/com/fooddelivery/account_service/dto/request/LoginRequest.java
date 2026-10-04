package com.fooddelivery.account_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request payload for user login")
public class LoginRequest {

    @Schema(description = "Registered user email", example = "amit.kumar@yopmail.com")
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "Password@123")
    private String password;
}
