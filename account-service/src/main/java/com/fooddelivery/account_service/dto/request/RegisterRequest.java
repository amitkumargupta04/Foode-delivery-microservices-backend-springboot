package com.fooddelivery.account_service.dto.request;

import com.fooddelivery.account_service.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User Registration Request Payload")
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "User email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Account password", example = "SecretPass123")
    private String password;

    @Schema(description = "User phone number", example = "+919876543210")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    @Schema(description = "Role assigned to the user", example = "ROLE_CUSTOMER")
    private Role role;
}
