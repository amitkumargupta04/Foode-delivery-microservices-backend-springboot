package com.fooddelivery.restaurant_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fooddelivery.restaurant_service.dto.event.RestaurantStatusUpdatedEvent;
import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.entity.MenuItem;
import com.fooddelivery.restaurant_service.entity.OutboxEvent;
import com.fooddelivery.restaurant_service.entity.Restaurant;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import com.fooddelivery.restaurant_service.exception.BadRequestException;
import com.fooddelivery.restaurant_service.exception.ResourceNotFoundException;
import com.fooddelivery.restaurant_service.mapper.MenuItemMapper;
import com.fooddelivery.restaurant_service.mapper.RestaurantMapper;
import com.fooddelivery.restaurant_service.repository.MenuItemRepository;
import com.fooddelivery.restaurant_service.repository.OutboxEventRepository;
import com.fooddelivery.restaurant_service.repository.RestaurantRepository;
import com.fooddelivery.restaurant_service.service.AdminRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminRestaurantServiceImpl implements AdminRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    public Page<RestaurantResponse> getAllRestaurants(
            RestaurantStatus status,
            String search,
            Pageable pageable
    ) {

        Page<Restaurant> restaurants;

        if (status != null && search != null && !search.isBlank()) {

            restaurants = restaurantRepository
                    .findByStatusAndNameContainingIgnoreCase(
                            status,
                            search,
                            pageable
                    );

        } else if (status != null) {

            restaurants = restaurantRepository
                    .findByStatus(status, pageable);

        } else if (search != null && !search.isBlank()) {

            restaurants = restaurantRepository
                    .findByNameContainingIgnoreCase(search, pageable);

        } else {

            restaurants = restaurantRepository.findAll(pageable);
        }

        return restaurants.map(restaurantMapper::toResponse);
    }
    @Override
    public RestaurantResponse getRestaurantById(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId)
        );
        return restaurantMapper.toResponse(restaurant);
    }
    @Override
    @Transactional
    public RestaurantResponse updateRestaurantStatus(
            UUID restaurantId,
            RestaurantStatus status
    ) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId)
        );
        restaurant.setStatus(status);
        restaurant.setUpdatedAt(LocalDateTime.now());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        RestaurantStatusUpdatedEvent event = RestaurantStatusUpdatedEvent.builder()
                        .type("RESTAURANT_STATUS_UPDATED")
                        .restaurantId(savedRestaurant.getId())
                        .ownerId(savedRestaurant.getOwnerId())
                        .restaurantName(savedRestaurant.getName())
                        .ownerEmail(savedRestaurant.getEmail())
                        .status(savedRestaurant.getStatus())
                        .rejectionReason(null)
                        .timestamp(LocalDateTime.now())
                        .build();

        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType("Restaurant")
                .aggregateId(savedRestaurant.getId().toString())
                .eventType("RestaurantStatusUpdated")
                .payload(payload)
                .published(false)
                .build();
        outboxEventRepository.save(outboxEvent);
        System.out.println("Event sent successfully...");
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    public Page<MenuItemResponse> getRestaurantMenuItems(
            UUID restaurantId,
            FoodType foodType,
            MenuItemStatus status,
            Pageable pageable
    ) {
        Page<MenuItem> menuItems;
        if (foodType != null && status != null) {
            menuItems = menuItemRepository
                    .findByRestaurantIdAndFoodTypeAndStatus(
                            restaurantId,
                            foodType,
                            status,
                            pageable
                    );
        } else if (foodType != null) {
            menuItems = menuItemRepository
                    .findByRestaurantIdAndFoodType(
                            restaurantId,
                            foodType,
                            pageable
                    );

        } else if (status != null) {
            menuItems = menuItemRepository
                    .findByRestaurantIdAndStatus(
                            restaurantId,
                            status,
                            pageable
                    );

        } else {
            menuItems = menuItemRepository
                    .findByRestaurantId(
                            restaurantId,
                            pageable
                    );
        }
        return menuItems.map(menuItemMapper::toResponse);
    }

    @Override
    @Transactional
    public MenuItemResponse updateMenuItemStatus(
            UUID restaurantId,
            UUID menuItemId,
            MenuItemStatus status
    ) {
        restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId)
        );
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new ResourceNotFoundException("Menu item not found with id: " + menuItemId)
        );
        if (!menuItem.getRestaurantId().equals(restaurantId)) {
            throw new BadRequestException("Menu item does not belong to this restaurant");
        }
        // Update status
        menuItem.setStatus(status);
        menuItem.setUpdatedAt(LocalDateTime.now());
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toResponse(savedMenuItem);
    }
    @Override
    @Transactional
    public void deleteRestaurant(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId)
        );
        restaurantRepository.delete(restaurant);
    }
}
