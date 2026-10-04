package com.fooddelivery.order_service.repository;

import com.fooddelivery.order_service.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByUserId(
            String userId,
            Pageable pageable
    );

    Optional<Order> findByIdAndUserId(
            UUID orderId,
            String userId
    );
    Page<Order> findByRestaurantId(
            UUID restaurantId,
            Pageable pageable
    );
}
