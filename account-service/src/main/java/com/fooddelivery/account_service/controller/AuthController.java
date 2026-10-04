package com.fooddelivery.account_service.controller;

import com.fooddelivery.account_service.dto.request.*;
import com.fooddelivery.account_service.dto.response.AuthResponse;
import com.fooddelivery.account_service.dto.response.StandardResponse;
import com.fooddelivery.account_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIs", description = "Endpoints for user registration, verification, login, and password reset")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with PENDING_VERIFICATION status and triggers verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<StandardResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        String data = authService.register(request);
        StandardResponse<String> response = new StandardResponse<>(
                true,
                "Registration initiated successfully.",
                HttpStatus.CREATED.value(),
                data,
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Validates the 6-digit OTP stored in Redis and marks the user as verified.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<StandardResponse<String>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        String data = authService.verifyOtp(request);
        StandardResponse<String> response = new StandardResponse<>(
                true,
                "OTP Verified Successfully",
                HttpStatus.CREATED.value(),
                data,
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "User Login", description = "Authenticates user credentials and returns JWT Bearer token upon success")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or unverified account"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<StandardResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse data = authService.login(request);
        StandardResponse<AuthResponse> response = new StandardResponse<>(
                true,
                "Login Successfully!",
                HttpStatus.OK.value(),
                data,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Forgot Password", description = "Sends a password-reset OTP to the user's registered email")
    @PostMapping("/forgot-password")
    public ResponseEntity<StandardResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String data = authService.forgotPassword(request);
        StandardResponse<String> response = new StandardResponse<>(
                true,
                "Please reset your password link shared on email",
                HttpStatus.OK.value(),
                data,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Reset Password", description = "Resets user password after OTP validation")
    @PostMapping("/reset-password")
    public ResponseEntity<StandardResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String data = authService.resetPassword(request);
        StandardResponse<String> response = new StandardResponse<>(
                true,
                "Password reset successfully!",
                HttpStatus.OK.value(),
                data,
                null
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
