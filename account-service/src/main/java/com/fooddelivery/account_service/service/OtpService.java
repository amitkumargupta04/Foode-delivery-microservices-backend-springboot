package com.fooddelivery.account_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private  final StringRedisTemplate redisTemplate;
    private static final String OTP_PREFIX = "OTP:";

    // 6-Digit OTP generate and in Redis 5 Min with TTL save
    public String generateAndSaveOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(900000) + 100000);
        String key = OTP_PREFIX + email;

        // Save to Redis with 5 minutes TTL
        redisTemplate.opsForValue().set(key, otp, 5, TimeUnit.MINUTES);
        return otp;
    }
    // OTP verify from redis
    public boolean validateOtp(String email, String inputOtp) {
        String key = OTP_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(inputOtp)) {
            redisTemplate.delete(key); // Verification successful -> Delete OTP
            return true;
        }
        return false;
    }
}
