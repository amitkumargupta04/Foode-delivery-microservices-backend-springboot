package com.fooddelivery.payment_service.repository;

import com.fooddelivery.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(UUID orderId);
    List<Payment> findByUserId(String userId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
