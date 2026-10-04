package com.fooddelivery.account_service.service.impl;

import com.fooddelivery.account_service.dto.event.PasswordResetRequestedEvent;
import com.fooddelivery.account_service.dto.event.UserRegisteredEvent;
import com.fooddelivery.account_service.dto.request.*;
import com.fooddelivery.account_service.dto.response.AuthResponse;
import com.fooddelivery.account_service.entity.OutboxEvent;
import com.fooddelivery.account_service.entity.User;
import com.fooddelivery.account_service.enums.AccountStatus;
import com.fooddelivery.account_service.exception.BadRequestException;
import com.fooddelivery.account_service.exception.DuplicateResourceException;
import com.fooddelivery.account_service.exception.ResourceNotFoundException;
import com.fooddelivery.account_service.kafka.KafkaTopics;
import com.fooddelivery.account_service.mapper.AuthMapper;
import com.fooddelivery.account_service.mapper.UserRegisteredEventMapper;
import com.fooddelivery.account_service.repository.OutboxEventRepository;
import com.fooddelivery.account_service.repository.UserRepository;
import com.fooddelivery.account_service.security.JwtUtils;
import com.fooddelivery.account_service.service.AuthService;
import com.fooddelivery.account_service.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpService  otpService;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserRegisteredEventMapper userRegisteredEventMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;
    private static final String OTP_KEY_PREFIX = "OTP:";

    @Override
    public String register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new DuplicateResourceException("Email Already Exists");
        }
        if(userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())){
            throw new DuplicateResourceException("Phone Number Already Exists");
        }
        User user = authMapper.toEntity(registerRequest, passwordEncoder, AccountStatus.PENDING_VERIFICATION);
        userRepository.save(user);
        String otp = otpService.generateAndSaveOtp(user.getEmail());

        UserRegisteredEvent event = userRegisteredEventMapper.toEvent(user, otp);
        String payload = objectMapper.writeValueAsString(event);
        System.out.println("Event published: " + payload);
        System.out.println("OTP: " + otp);
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType("User")
                .aggregateId(user.getId().toString())
                .eventType(KafkaTopics.USER_REGISTERED)
                .payload(payload)
                .published(false)
                .build();

        outboxEventRepository.save(outboxEvent);

        return "Registration successful. Please verify the OTP sent to your registered email address.";
    }
    @Override
    @Transactional
    public String verifyOtp(VerifyOtpRequest request) {
        String redisKey = "OTP:" + request.getEmail();
        String cachedOtp = redisTemplate.opsForValue().get(redisKey);
        if (cachedOtp == null) {
            log.warn("OTP verification failed: OTP expired or not found for email: {}", request.getEmail());
            throw new BadRequestException("OTP has expired or is invalid");
        }
        if (!cachedOtp.equals(request.getOtp())) {
            log.warn("OTP verification failed: Incorrect OTP entered for email: {}", request.getEmail());
            throw new BadRequestException("Invalid OTP entered");
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + request.getEmail())
        );
        user.setEmailVerified(true);
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        redisTemplate.delete(redisKey);
        return "Otp verified successfully now you can login!";
    }
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + request.getEmail())
        );
        if (!user.isEmailVerified()) {
            throw new BadRequestException("Please verify your email/OTP before logging in");
        }
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Your account is not active. Please contact support");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400000)
                .build();
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + request.getEmail())
        );
        String otp = String.format("%06d", new Random().nextInt(900000) + 100000);
        redisTemplate.opsForValue().set("OTP_RESET:" + user.getEmail(), otp, 10, TimeUnit.MINUTES);

        PasswordResetRequestedEvent event = PasswordResetRequestedEvent.builder()
                .type("PASSWORD_RESET_REQUESTED")
                .userId(user.getId().toString())
                .email(user.getEmail())
                .otp(otp)
                .timestamp(LocalDateTime.now())
                .build();
        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType("User")
                .aggregateId(user.getId().toString())
                .eventType(KafkaTopics.PASSWORD_RESET_REQUESTED)
                .payload(payload)
                .published(false)
                .build();

        outboxEventRepository.save(outboxEvent);

        return "Password reset OTP sent to your registered email";
    }

    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
        String redisKey = "OTP_RESET:" + request.getEmail();
        String cachedOtp = redisTemplate.opsForValue().get(redisKey);
        if (cachedOtp == null) {
            throw new BadRequestException("OTP has expired or is invalid");
        }
        if (!cachedOtp.equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP entered");
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + request.getEmail())
        );
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        redisTemplate.delete(redisKey);
        return "Password reset successfully. You can now login with your new password";
    }
}
