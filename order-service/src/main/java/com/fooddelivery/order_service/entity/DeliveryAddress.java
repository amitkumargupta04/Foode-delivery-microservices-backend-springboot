package com.fooddelivery.order_service.entity;

import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryAddress {

    private String addressLine;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
}
