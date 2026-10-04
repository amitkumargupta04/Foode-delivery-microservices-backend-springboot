package com.fooddelivery.order_service.controller;

import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.dto.response.ResponseBuilder;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import com.fooddelivery.order_service.enums.PaymentStatus;
import com.fooddelivery.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/orders")
@RequiredArgsConstructor
@Hidden
public class InternalOrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<StandardResponse<OrderResponse>> getOrderById(
            @PathVariable UUID orderId
    ) {
        OrderResponse order = orderService.getOrderById(orderId);

        return ResponseBuilder.success(
                "Order fetched successfully",
                order,
                HttpStatus.OK
        );
    }
    @PatchMapping("/{orderId}/payment-status")
    public ResponseEntity<StandardResponse<Void>> updatePaymentStatus(
            @PathVariable UUID orderId,
            @RequestParam PaymentStatus paymentStatus
    ) {
        orderService.updatePaymentStatus(orderId, paymentStatus);
        return ResponseBuilder.success(
                "Payment status updated successfully",
                null,
                HttpStatus.OK
        );
    }
}
