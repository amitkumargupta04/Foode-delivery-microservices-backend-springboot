package com.fooddelivery.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.notification_service.dto.event.UserRegisteredEvent;
import com.fooddelivery.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "user.registered", groupId = "notification-group")
    public void consumeUserRegisteredEvent(String message) {
        log.info("Received event from Kafka: {}", message);
        try {
            String jsonPayload = message;
            if (message.startsWith("\"") && message.endsWith("\"")) {
                jsonPayload = objectMapper.readValue(message, String.class);
            }
            UserRegisteredEvent event = objectMapper.readValue(jsonPayload, UserRegisteredEvent.class);
            emailService.sendOtpEmail(event.getEmail(), event.getOtp());
            log.info("Successfully processed event for: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", message, e);
        }
    }
}
