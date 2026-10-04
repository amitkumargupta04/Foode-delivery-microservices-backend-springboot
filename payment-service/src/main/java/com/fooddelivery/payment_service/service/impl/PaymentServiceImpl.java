package com.fooddelivery.payment_service.service.impl;

import com.fooddelivery.payment_service.client.OrderClient;
import com.fooddelivery.payment_service.dto.request.PaymentRequest;
import com.fooddelivery.payment_service.dto.response.OrderResponse;
import com.fooddelivery.payment_service.dto.response.PaymentResponse;
import com.fooddelivery.payment_service.dto.response.StandardResponse;
import com.fooddelivery.payment_service.entity.Payment;
import com.fooddelivery.payment_service.enums.OrderStatus;
import com.fooddelivery.payment_service.enums.PaymentStatus;
import com.fooddelivery.payment_service.exception.BadRequestException;
import com.fooddelivery.payment_service.exception.ResourceNotFoundException;
import com.fooddelivery.payment_service.mapper.PaymentMapper;
import com.fooddelivery.payment_service.repository.PaymentRepository;
import com.fooddelivery.payment_service.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final RazorpayClient razorpayClient;
    private final PaymentMapper paymentMapper;


    @Override
    @Transactional
    public PaymentResponse createPayment(String userId, PaymentRequest request) {
        ResponseEntity<StandardResponse<OrderResponse>> response = orderClient.getOrderById(request.getOrderId());
        OrderResponse order = response.getBody().getData();
        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to this user");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Payment cannot be created for cancelled order");
        }
        BigDecimal amount = order.getTotalAmount();
        try {
            RazorpayClient client = razorpayClient;
            JSONObject options = new JSONObject();
            options.put("amount", amount.multiply(BigDecimal.valueOf(100)).longValue());
            options.put("currency", "INR");
            options.put("receipt", order.getId().toString());
            Order razorpayOrder = client.orders.create(options);
            Payment payment = Payment.builder()
                    .orderId(order.getId())
                    .userId(userId)
                    .amount(amount)
                    .status(PaymentStatus.PENDING)
                    .paymentMethod(request.getPaymentMethod())
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Payment savedPayment = paymentRepository.save(payment);
            return paymentMapper.toResponse(savedPayment);
        } catch (RazorpayException e) {
            throw new BadRequestException("Failed to create Razorpay order");
        }
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found with id: " + paymentId)
        );
        return toResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found for order: " + orderId)
        );
        return toResponse(payment);
    }
    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
