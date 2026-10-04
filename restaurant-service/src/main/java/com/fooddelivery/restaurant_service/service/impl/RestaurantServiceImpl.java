package com.fooddelivery.restaurant_service.service.impl;

import com.fooddelivery.restaurant_service.dto.request.RestaurantRequest;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.entity.Restaurant;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import com.fooddelivery.restaurant_service.exception.BadRequestException;
import com.fooddelivery.restaurant_service.exception.ResourceNotFoundException;
import com.fooddelivery.restaurant_service.mapper.RestaurantMapper;
import com.fooddelivery.restaurant_service.repository.RestaurantRepository;
import com.fooddelivery.restaurant_service.service.RestaurantService;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantResponse onboarding(String ownerId, RestaurantRequest request) {

        if (restaurantRepository.findByOwnerId(ownerId).isPresent()) {
            throw new BadRequestException("Restaurant already onboarded for this owner");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .ownerId(ownerId)
                .phone(request.getPhone())
                .email(request.getEmail())
                .description(request.getDescription())
                .address(restaurantMapper.toAddress(request.getAddress()))
                .status(RestaurantStatus.PENDING)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        System.out.println("Before onboarding");
        return restaurantMapper.toResponse(
                restaurantRepository.save(restaurant)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getProfile(String ownerId) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    public RestaurantResponse updateProfile(
            String ownerId,
            RestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        restaurant.setName(request.getName());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setDescription(request.getDescription());

        if (request.getAddress() != null) {
            restaurant.setAddress(
                    restaurantMapper.toAddress(request.getAddress())
            );
        }

        restaurant.setUpdatedAt(LocalDateTime.now());

        return restaurantMapper.toResponse(
                restaurantRepository.save(restaurant)
        );
    }

    @Override
    public void deleteProfile(String ownerId) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        restaurantRepository.delete(restaurant);
    }

    // Order management via Owner [Internal service]
    @Override
    public RestaurantResponse getRestaurantByOwnerId(String ownerId) {
        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurant not found for owner: " + ownerId)
        );
        return restaurantMapper.toResponse(restaurant);
    }
}
