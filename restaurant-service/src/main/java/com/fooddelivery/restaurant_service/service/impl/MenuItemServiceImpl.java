package com.fooddelivery.restaurant_service.service.impl;

import com.fooddelivery.restaurant_service.dto.request.MenuItemRequest;
import com.fooddelivery.restaurant_service.dto.response.MenuItemResponse;
import com.fooddelivery.restaurant_service.dto.response.StandardResponse;
import com.fooddelivery.restaurant_service.entity.MenuItem;
import com.fooddelivery.restaurant_service.entity.Restaurant;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import com.fooddelivery.restaurant_service.exception.BadRequestException;
import com.fooddelivery.restaurant_service.mapper.MenuItemMapper;
import com.fooddelivery.restaurant_service.repository.MenuItemRepository;
import com.fooddelivery.restaurant_service.repository.RestaurantRepository;
import com.fooddelivery.restaurant_service.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    public MenuItemResponse createMenuItem(
            String ownerId,
            MenuItemRequest request
    ) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId).orElseThrow(
                () -> new BadRequestException("Restaurant not found")
        );

        if (!restaurant.getStatus().equals(RestaurantStatus.APPROVED)) {
            throw new BadRequestException(
                    "Restaurant must be approved before adding menu items"
            );
        }

        if (menuItemRepository.existsByRestaurantIdAndName(
                restaurant.getId(),
                request.getName()
        )) {
            throw new BadRequestException(
                    "Menu item already exists"
            );
        }

        MenuItem menuItem = MenuItem.builder()
                .restaurantId(restaurant.getId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .foodType(request.getFoodType())
                .status(MenuItemStatus.AVAILABLE)
                .imageUrl(request.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return menuItemMapper.toResponse(
                menuItemRepository.save(menuItem)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemResponse> getMenuItems(
            String ownerId,
            FoodType foodType,
            MenuItemStatus status,
            Pageable pageable
    ) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new BadRequestException("Restaurant not found"));

        Page<MenuItem> menuItems;

        if (foodType != null) {
            menuItems = menuItemRepository.findByRestaurantIdAndFoodType(
                    restaurant.getId(),
                    foodType,
                    pageable
            );
        } else if (status != null) {
            menuItems = menuItemRepository.findByRestaurantIdAndStatus(
                    restaurant.getId(),
                    status,
                    pageable
            );
        } else {
            menuItems = menuItemRepository.findByRestaurantId(
                    restaurant.getId(),
                    pageable
            );
        }

        return menuItems.map(menuItemMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(
            String ownerId,
            UUID menuItemId
    ) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new BadRequestException("Restaurant not found"));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() ->
                        new BadRequestException("Menu item not found"));

        if (!menuItem.getRestaurantId().equals(restaurant.getId())) {
            throw new BadRequestException(
                    "Menu item does not belong to your restaurant"
            );
        }

        return menuItemMapper.toResponse(menuItem);
    }

    @Override
    public MenuItemResponse updateMenuItem(
            String ownerId,
            UUID menuItemId,
            MenuItemRequest request
    ) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new BadRequestException("Restaurant not found"));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() ->
                        new BadRequestException("Menu item not found"));

        if (!menuItem.getRestaurantId().equals(restaurant.getId())) {
            throw new BadRequestException(
                    "Menu item does not belong to your restaurant"
            );
        }

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setFoodType(request.getFoodType());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setUpdatedAt(LocalDateTime.now());

        return menuItemMapper.toResponse(
                menuItemRepository.save(menuItem)
        );
    }

    @Override
    public void deleteMenuItem(
            String ownerId,
            UUID menuItemId
    ) {

        Restaurant restaurant = restaurantRepository.findByOwnerId(ownerId)
                .orElseThrow(() ->
                        new BadRequestException("Restaurant not found"));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() ->
                        new BadRequestException("Menu item not found"));

        if (!menuItem.getRestaurantId().equals(restaurant.getId())) {
            throw new BadRequestException(
                    "Menu item does not belong to your restaurant"
            );
        }

        menuItemRepository.delete(menuItem);
    }
}
