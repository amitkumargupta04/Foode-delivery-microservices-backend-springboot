package com.fooddelivery.restaurant_service.controller;

import com.fooddelivery.restaurant_service.dto.request.RestaurantRequest;
import com.fooddelivery.restaurant_service.dto.response.ResponseBuilder;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.dto.response.StandardResponse;
import com.fooddelivery.restaurant_service.service.RestaurantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant onboarding flow")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/onboarding")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<StandardResponse<RestaurantResponse>> onboarding(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RestaurantRequest request
    ) {
        String ownerId = jwt.getSubject();

        return ResponseBuilder.success(
                "Restaurant onboarding submitted successfully",
                restaurantService.onboarding(ownerId, request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<StandardResponse<RestaurantResponse>> getProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();

        return ResponseBuilder.success(
                "Restaurant profile fetched successfully",
                restaurantService.getProfile(ownerId),
                HttpStatus.OK
        );
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public  ResponseEntity<StandardResponse<RestaurantResponse>> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RestaurantRequest request
    ) {
        String ownerId = jwt.getSubject();

        return ResponseBuilder.success(
                "Restaurant profile updated successfully",
                restaurantService.updateProfile(ownerId, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<StandardResponse<Void>> deleteProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String ownerId = jwt.getSubject();

        restaurantService.deleteProfile(ownerId);

        return ResponseBuilder.success(
                "Restaurant profile deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}
