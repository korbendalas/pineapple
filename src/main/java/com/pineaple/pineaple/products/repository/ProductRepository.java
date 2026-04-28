package com.pineaple.pineaple.products.repository;

import com.pineaple.pineaple.products.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository  extends JpaRepository<Product, Long> {

    Optional<Product> findByProductId(UUID productId);

    void deleteByProductId(UUID productId);
}
