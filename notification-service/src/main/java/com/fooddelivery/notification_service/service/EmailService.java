package com.fooddelivery.notification_service.service;

import com.fooddelivery.notification_service.enums.RestaurantStatus;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
    void sendPasswordResetOtpEmail(String toEmail, String otp);
    void sendRestaurantStatusEmail(
            String ownerEmail,
            String ownerId,
            String restaurantName,
            RestaurantStatus status,
            String rejectionReason
    );
}
