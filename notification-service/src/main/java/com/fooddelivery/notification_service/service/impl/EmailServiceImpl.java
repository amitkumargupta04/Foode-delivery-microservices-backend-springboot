package com.fooddelivery.notification_service.service.impl;

import com.fooddelivery.notification_service.enums.RestaurantStatus;
import com.fooddelivery.notification_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            // 1. Thymeleaf Template context setup
            Context context = new Context();
            context.setVariable("name", extractNameFromEmail(toEmail));
            context.setVariable("otp", otp);

            // 2. Process HTML Template (src/main/resources/templates/otp-email.html)
            String htmlContent = templateEngine.process("otp-email", context);

            // 3. Prepare MIME Message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("OTP Verification - Food Delivery");
            helper.setText(htmlContent, true);

            // 4. Send Email
            mailSender.send(message);
            log.info("OTP verification email successfully sent to {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}", toEmail, e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }
    @Override
    public void sendPasswordResetOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("name", extractNameFromEmail(toEmail));
            context.setVariable("otp", otp);


            String htmlContent = templateEngine.process("password-reset-email", context);

            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - Food Delivery App");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Password reset OTP email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendRestaurantStatusEmail(String ownerEmail, String ownerId, String restaurantName,
            RestaurantStatus status,
            String rejectionReason
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("ownerId", ownerId);
            context.setVariable("name", extractNameFromEmail(ownerEmail));
            context.setVariable("status", status.name());
            //context.setVariable("rejectionReason", rejectionReason != null ? rejectionReason : "N/A");

            // Single Thymeleaf Template
            String htmlContent = templateEngine.process("restaurant-status-email", context);

            helper.setTo(ownerEmail);
            helper.setSubject("Restaurant Application Status: " + status.name() + " - " + restaurantName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Status email [{}] sent successfully to: {}", status, ownerEmail);

        } catch (Exception e) {
            log.error("Failed to send status update email to: {}", ownerEmail, e);
        }
    }

    // Helper method to extract greeting name from email (e.g., amitkumargupta -> Amit Kumar Gupta)
    private String extractNameFromEmail(String email) {
        if (email == null || !email.contains("@")) return "User";
        String namePart = email.split("@")[0].replaceAll("[0-9]", "");
        if (namePart.isEmpty()) return "User";
        return namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
    }
}
