package com.fooddelivery.notification_service.dto.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisteredEvent {

    private String type;
    private String userId;
    private String email;
    private String otp;
    private LocalDateTime timestamp;
}
