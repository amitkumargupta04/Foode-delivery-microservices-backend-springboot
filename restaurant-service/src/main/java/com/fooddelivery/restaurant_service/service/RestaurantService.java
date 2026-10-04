package com.fooddelivery.restaurant_service.service;

import com.fooddelivery.restaurant_service.dto.request.RestaurantRequest;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;

public interface RestaurantService {
    RestaurantResponse onboarding(
            String ownerId,
            RestaurantRequest request
    );

    RestaurantResponse getProfile(
            String ownerId
    );

    RestaurantResponse updateProfile(
            String ownerId,
            RestaurantRequest request
    );

    void deleteProfile(
            String ownerId
    );

    // Restaurant owner order management APIs [Internal-function]
    RestaurantResponse getRestaurantByOwnerId(String ownerId);
}
