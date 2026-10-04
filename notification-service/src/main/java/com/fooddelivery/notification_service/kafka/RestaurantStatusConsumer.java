package com.fooddelivery.notification_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooddelivery.notification_service.dto.event.RestaurantStatusUpdatedEvent;
import com.fooddelivery.notification_service.enums.RestaurantStatus;
import com.fooddelivery.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class RestaurantStatusConsumer {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "RestaurantStatusUpdated", groupId = "notification-group")
    public void consumeRestaurantStatusEvent(String message) {
        log.info("Received Kafka event for restaurant status: {}", message);
        try {
            String jsonPayload = message;
            if (message.startsWith("\"") && message.endsWith("\"")) {
                jsonPayload = objectMapper.readValue(message, String.class);
            }
            RestaurantStatusUpdatedEvent event = objectMapper.readValue(jsonPayload, RestaurantStatusUpdatedEvent.class);
            // Single method call
            emailService.sendRestaurantStatusEmail(
                    event.getOwnerEmail(),
                    event.getOwnerId(),
                    event.getRestaurantName(),
                    event.getStatus(),
                    event.getRejectionReason()
            );
            log.info("Successfully processed status update email for restaurantId: {}", event.getRestaurantId());
        } catch (Exception e) {
            log.error("Error processing restaurant status Kafka message: {}", message, e);
        }
    }
}
