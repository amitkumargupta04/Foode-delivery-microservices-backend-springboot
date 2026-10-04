package com.fooddelivery.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID restaurantId;

    @Column(nullable = false)
    private UUID menuItemId;

    @Column(nullable = false)
    private Integer quantity;

    @Builder.Default
    @Column(nullable = false)
    private Boolean available = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
