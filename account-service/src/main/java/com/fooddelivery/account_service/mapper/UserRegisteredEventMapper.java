package com.fooddelivery.account_service.mapper;

import com.fooddelivery.account_service.dto.event.UserRegisteredEvent;
import com.fooddelivery.account_service.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserRegisteredEventMapper {
    public UserRegisteredEvent toEvent(User user, String otp) {
        return UserRegisteredEvent.builder()
                .type("USER_REGISTERED")
                .userId(user.getId().toString())
                .email(user.getEmail())
                .otp(otp)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
