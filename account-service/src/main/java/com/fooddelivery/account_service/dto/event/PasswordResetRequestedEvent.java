package com.fooddelivery.account_service.dto.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequestedEvent {
    private String type;
    private String userId;
    private String email;
    private String otp;
    private LocalDateTime timestamp;
}
