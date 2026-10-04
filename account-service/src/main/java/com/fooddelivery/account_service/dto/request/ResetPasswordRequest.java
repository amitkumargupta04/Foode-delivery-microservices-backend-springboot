package com.fooddelivery.account_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request payload to reset password using OTP")
public class ResetPasswordRequest {

    @Schema(description = "Registered user email", example = "amit.kumar@yopmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "6-digit OTP received on email", example = "654321")
    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;

    @Schema(description = "New password", example = "NewPassword@123")
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

    @Schema(description = "Confirm new password", example = "NewPassword@123")
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
