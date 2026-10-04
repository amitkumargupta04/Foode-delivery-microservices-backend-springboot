package com.fooddelivery.restaurant_service.repository;

import com.fooddelivery.restaurant_service.entity.Restaurant;
import com.fooddelivery.restaurant_service.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByOwnerId(String ownerId);

   /* For Admin Restaurant Management */

    Page<Restaurant> findByStatus(
            RestaurantStatus status,
            Pageable pageable
    );

    Page<Restaurant> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Restaurant> findByStatusAndNameContainingIgnoreCase(
            RestaurantStatus status,
            String name,
            Pageable pageable
    );

}
