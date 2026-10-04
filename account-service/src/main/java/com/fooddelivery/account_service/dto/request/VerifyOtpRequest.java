package com.fooddelivery.account_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User VerifyOtp payload")
public class VerifyOtpRequest {

    @NotBlank(message = "Email is required")
    @Email
    @Schema(description = "User email address", example = "joh,deo@yopmail.com")
    private String email;

    @NotBlank(message = "OTP is required")
    @Schema(description = "User otp", example = "272206")
    private String otp;
}
