package com.fooddelivery.account_service.service;

import com.fooddelivery.account_service.dto.request.*;
import com.fooddelivery.account_service.dto.response.AuthResponse;

public interface AuthService {

    public String register(RegisterRequest registerRequest);
    public String verifyOtp(VerifyOtpRequest verifyOtpRequest);
    AuthResponse login(LoginRequest loginRequest);
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    public String resetPassword(ResetPasswordRequest resetPasswordRequest);
}
