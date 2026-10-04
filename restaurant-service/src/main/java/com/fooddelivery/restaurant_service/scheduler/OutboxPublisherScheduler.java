package com.fooddelivery.restaurant_service.scheduler;


import com.fooddelivery.restaurant_service.entity.OutboxEvent;
import com.fooddelivery.restaurant_service.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisherScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByPublishedFalse();
        if (events.isEmpty()) {
            return;
        }
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getAggregateId(), event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                event.setPublished(true);
                                outboxEventRepository.save(event);
                                log.info("Successfully published event ID: {} to topic: {}", event.getId(), event.getEventType());
                            } else {
                                log.error("Failed to publish event ID: {}", event.getId(), ex);
                            }
                        });
            } catch (Exception e) {
                log.error("Error publishing event ID: {}", event.getId(), e);
            }
        }
    }
}
