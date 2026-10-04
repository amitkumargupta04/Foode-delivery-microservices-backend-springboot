package com.fooddelivery.order_service.controller;

import com.fooddelivery.order_service.dto.request.OrderRequest;
import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.dto.response.PaymentResponse;
import com.fooddelivery.order_service.dto.response.ResponseBuilder;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import com.fooddelivery.order_service.enums.PaymentMethod;
import com.fooddelivery.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(
        name = "Order APIs",
        description = "Customer order management APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create order",
            description = "Create a new order for the authenticated customer"
    )
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<OrderResponse>> createOrder(
            @Valid
            @RequestBody
            OrderRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        OrderResponse order = orderService.createOrder(userId, request);
        return ResponseBuilder.success(
                "Order created successfully",
                order,
                HttpStatus.CREATED
        );
    }
    @Operation(
            summary = "Get my orders",
            description = "Fetch all orders placed by the authenticated customer"
    )
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<List<OrderResponse>>> getMyOrders(
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDirection,

            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(
                        page,
                        size,
                        Sort.by(direction, sortBy)
        );
        Page<OrderResponse> orders = orderService.getMyOrders(userId, pageable);
        return ResponseBuilder.successPaginated(
                "Orders fetched successfully",
                orders
        );
    }
    @Operation(
            summary = "Get my order by ID",
            description = "Fetch a specific order belonging to the authenticated customer"
    )
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<OrderResponse>> getMyOrderById(
            @Parameter(description = "Order ID", required = true)
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        OrderResponse order = orderService.getMyOrderById(userId, orderId);
        return ResponseBuilder.success(
                "Order fetched successfully",
                order,
                HttpStatus.OK
        );
    }
    @Operation(
            summary = "Cancel order",
            description = "Cancel an order placed by the authenticated customer"
    )
    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<Void>> cancelOrder(
            @Parameter(description = "Order ID", required = true)
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        orderService.cancelOrder(userId, orderId);
        return ResponseBuilder.success(
                "Order cancelled successfully",
                null,
                HttpStatus.OK
        );
    }

    @PostMapping("/{orderId}/payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<PaymentResponse>> createPayment(
            @PathVariable UUID orderId,
            @RequestParam PaymentMethod paymentMethod,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        PaymentResponse payment = orderService.createPayment(
                        userId,
                        orderId,
                        paymentMethod
        );
        return ResponseBuilder.success(
                "Payment created successfully",
                payment,
                HttpStatus.CREATED
        );
    }
    @GetMapping("/{orderId}/payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StandardResponse<PaymentResponse>> getPaymentByOrderId(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        PaymentResponse payment = orderService.getPaymentByOrderId(
                        userId,
                        orderId
        );
        return ResponseBuilder.success(
                "Payment fetched successfully",
                payment,
                HttpStatus.OK
        );
    }
}
