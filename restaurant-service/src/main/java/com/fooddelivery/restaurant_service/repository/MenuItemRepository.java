package com.fooddelivery.restaurant_service.repository;

import com.fooddelivery.restaurant_service.entity.MenuItem;
import com.fooddelivery.restaurant_service.enums.FoodType;
import com.fooddelivery.restaurant_service.enums.MenuItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    Page<MenuItem> findByRestaurantId(UUID restaurantId, Pageable pageable);
    Page<MenuItem> findByRestaurantIdAndFoodType(UUID restaurantId, FoodType foodType, Pageable pageable);
    Page<MenuItem> findByRestaurantIdAndStatus(UUID restaurantId, MenuItemStatus status, Pageable pageable);
    boolean existsByRestaurantIdAndName(UUID restaurantId, String name);
    Page<MenuItem> findByRestaurantIdAndFoodTypeAndStatus(
            UUID restaurantId,
            FoodType foodType,
            MenuItemStatus status,
            Pageable pageable
    );
}
