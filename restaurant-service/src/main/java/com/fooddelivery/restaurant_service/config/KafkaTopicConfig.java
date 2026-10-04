package com.fooddelivery.restaurant_service.config;

import com.fooddelivery.restaurant_service.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic restaurantStatusUpdatedTopic() {
        return TopicBuilder
                .name(KafkaTopics.RESTAURANT_STATUS_UPDATED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
