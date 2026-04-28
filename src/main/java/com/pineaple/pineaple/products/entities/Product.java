package com.pineaple.pineaple.products.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "products")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "product_id", nullable = false, updatable = false, unique = true)
    private UUID productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Product create(String name, String description, BigDecimal price, String currency) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name must not be blank");
        if (price == null) throw new IllegalArgumentException("Product price must not be null");
        Product p = new Product();
        p.productId = UUID.randomUUID();
        p.name = name;
        p.description = description;
        p.price = price.stripTrailingZeros();
        p.currency = currency;
        p.createdAt = LocalDateTime.now();
        return p;
    }

}
