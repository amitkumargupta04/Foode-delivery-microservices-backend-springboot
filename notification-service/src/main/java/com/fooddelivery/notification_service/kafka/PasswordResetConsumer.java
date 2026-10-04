package com.fooddelivery.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.notification_service.dto.event.PasswordResetRequestedEvent;
import com.fooddelivery.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordResetConsumer {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "password.reset.requested", groupId = "notification-group")
    public void consumePasswordResetEvent(String message) {
        log.info("Received password reset event: {}", message);
        try {
            String jsonPayload = message;
            if (message.startsWith("\"") && message.endsWith("\"")) {
                jsonPayload = objectMapper.readValue(message, String.class);
            }
            PasswordResetRequestedEvent event = objectMapper.readValue(jsonPayload, PasswordResetRequestedEvent.class);
            // Trigger Reset Email
            emailService.sendPasswordResetOtpEmail(event.getEmail(), event.getOtp());
            log.info("Successfully processed password reset email for: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to process password reset Kafka message: {}", message, e);
        }
    }
}
