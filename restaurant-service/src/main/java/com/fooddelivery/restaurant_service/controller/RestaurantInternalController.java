package com.fooddelivery.restaurant_service.controller;

import com.fooddelivery.restaurant_service.dto.response.ResponseBuilder;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.dto.response.StandardResponse;
import com.fooddelivery.restaurant_service.service.RestaurantService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/v1/internal/restaurants")
@Tag(
        name = "Restaurant Internal Management",
        description = "Internal service-to-service APIs"
)
@RequiredArgsConstructor
public class RestaurantInternalController {

    private final RestaurantService restaurantService;

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<StandardResponse<RestaurantResponse>> getRestaurantByOwnerId(
            @PathVariable String ownerId
    ) {
        RestaurantResponse restaurant = restaurantService.getRestaurantByOwnerId(ownerId);

        return ResponseBuilder.success(
                "Restaurant fetched successfully",
                restaurant,
                HttpStatus.OK
        );
    }
}
