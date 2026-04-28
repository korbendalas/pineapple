package com.pineaple.pineaple.products.controllers;

import com.pineaple.pineaple.products.dto.CreateProductRequest;
import com.pineaple.pineaple.products.dto.ProductNotFoundException;
import com.pineaple.pineaple.products.dto.ProductResponse;
import com.pineaple.pineaple.products.dto.UpdateProductRequest;
import com.pineaple.pineaple.products.entities.Product;
import com.pineaple.pineaple.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request.name(), request.description(), request.price(), request.currency());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product));
    }

    @GetMapping("/{productId}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable UUID productId) {
        Product product = productService.findProductByProductId(productId);

        return ResponseEntity.ok(ProductResponse.from(product));
    }

public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
    Page<ProductResponse> page = productService.getAllProducts(pageable).map(ProductResponse::from);
    return ResponseEntity.ok(page);

}

    @DeleteMapping("/{productId}")
    ResponseEntity<Void> deleteProductById(@PathVariable UUID productId){
        productService.deleteProductByProductId(productId);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProductPartially(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request
    ){
        Product product = productService.updateProductPartially(productId, request);

        return ResponseEntity.ok(ProductResponse.from(product));
    }


}
