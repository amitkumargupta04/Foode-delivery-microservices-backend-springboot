package com.fooddelivery.restaurant_service.mapper;

import com.fooddelivery.restaurant_service.dto.request.AddressRequest;
import com.fooddelivery.restaurant_service.dto.response.AddressResponse;
import com.fooddelivery.restaurant_service.dto.response.RestaurantResponse;
import com.fooddelivery.restaurant_service.entity.Address;
import com.fooddelivery.restaurant_service.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    public Address toAddress(AddressRequest request) {
        return Address.builder()
                .addressLine(request.getAddressLine())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }

    public AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .ownerId(restaurant.getOwnerId())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .description(restaurant.getDescription())
                .status(restaurant.getStatus())
                .active(restaurant.getActive())
                .address(toAddressResponse(restaurant.getAddress()))
                .build();
    }
}
