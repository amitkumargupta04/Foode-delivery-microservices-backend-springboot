package com.fooddelivery.account_service.mapper;

import com.fooddelivery.account_service.dto.request.RegisterRequest;
import com.fooddelivery.account_service.entity.User;
import com.fooddelivery.account_service.enums.AccountStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AuthMapper {

    public User toEntity(RegisterRequest request, PasswordEncoder passwordEncoder, AccountStatus  accountStatus) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());
        user.setStatus(accountStatus);
        user.setEmailVerified(false);
        return user;

    }
}
