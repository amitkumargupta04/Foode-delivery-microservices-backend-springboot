package com.fooddelivery.order_service.controller;

import com.fooddelivery.order_service.dto.response.OrderResponse;
import com.fooddelivery.order_service.dto.response.ResponseBuilder;
import com.fooddelivery.order_service.dto.response.StandardResponse;
import com.fooddelivery.order_service.enums.OrderStatus;
import com.fooddelivery.order_service.service.RestaurantOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/restaurant/orders")
@RequiredArgsConstructor
@Tag(
        name = "Order Management",
        description = "Restaurant owner order management APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class RestaurantOrderController {

    private final RestaurantOrderService restaurantOrderService;

    @GetMapping
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<StandardResponse<List<OrderResponse>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String ownerId = jwt.getSubject();
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(direction, sortBy)
                );

        Page<OrderResponse> orders = restaurantOrderService.getRestaurantOrders(
                        ownerId,
                        pageable
        );

        return ResponseBuilder.successPaginated(
                "Restaurant orders fetched successfully",
                orders
        );
    }


    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<StandardResponse<OrderResponse>> getOrderById(
            @PathVariable UUID orderId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();
        OrderResponse order = restaurantOrderService.getRestaurantOrderById(ownerId, orderId);
        return ResponseBuilder.success(
                "Order fetched successfully",
                order,
                HttpStatus.OK
        );
    }


    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<StandardResponse<OrderResponse>> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();
        OrderResponse order = restaurantOrderService.updateOrderStatus(
                        ownerId,
                        orderId,
                        status
        );

        return ResponseBuilder.success(
                "Order status updated successfully",
                order,
                HttpStatus.OK
        );
    }
}
