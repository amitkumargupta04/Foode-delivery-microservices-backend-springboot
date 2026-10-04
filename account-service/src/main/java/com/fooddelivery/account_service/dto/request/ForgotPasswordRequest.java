package com.fooddelivery.account_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request payload to trigger forgot password OTP")
public class ForgotPasswordRequest {

    @Schema(description = "Registered user email", example = "amit.kumar@yopmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
