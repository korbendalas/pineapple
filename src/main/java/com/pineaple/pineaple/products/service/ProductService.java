package com.pineaple.pineaple.products.service;

import com.pineaple.pineaple.products.dto.ProductNotFoundException;
import com.pineaple.pineaple.products.dto.UpdateProductRequest;
import com.pineaple.pineaple.products.entities.Product;
import com.pineaple.pineaple.products.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    @Transactional
    public Product createProduct(String name, String description, BigDecimal price, String currency) {
        Product product = Product.create(name, description, price, currency);
        log.info("Product created. productId={}, name={}, price={}", product.getProductId(), product.getName(), product.getPrice());
        Product saved = productRepository.save(product);
        return saved;
    }

    public Product findProductByProductId(UUID productId){
        return productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    @Transactional
    public void deleteProductByProductId(UUID productId){
        if (productRepository.findByProductId(productId).isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteByProductId(productId);
        log.info("Product deleted. productId={}", productId);
    }

    @Transactional
    public Product updateProductPartially(UUID productId, UpdateProductRequest request) {
        Product product = findProductByProductId(productId);

        if (request.name() != null) {
            product.setName(request.name());
        }

        if (request.description() != null) {
            product.setDescription(request.description());
        }

        if (request.price() != null) {
            product.setPrice(request.price());
        }

        if (request.currency() != null) {
            product.setCurrency(request.currency());
        }

        return product;
    }

}
