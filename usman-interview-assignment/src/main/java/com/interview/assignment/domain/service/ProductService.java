package com.interview.assignment.domain.service;

import com.interview.assignment.domain.dto.ProductRequest;
import com.interview.assignment.domain.exception.ResourceNotFoundException;
import com.interview.assignment.repository.ProductRepository;
import com.interview.assignment.repository.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        Product saved = productRepository.save(product);
        log.info("Product saved successfully");
        return saved;
    }

    @Cacheable(value = "products")
    public List<Product> getAll() {
        log.info("Finding all products...");
        return productRepository.findByActiveTrue();
    }

    @Cacheable(value = "product-id", key="#id")
    public Optional<Product> getProductById(Long id) {
        log.info("Finding product by id...");
        return productRepository.findById(id);
    }

    @Cacheable(value = "product-search", key = "#name")
    public List<Product> getProductByName(String name) {
        log.info("Finding all products by name...");
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    @Cacheable(value = "product-range", key = "#min.toPlainString() + '-' + #max.toPlainString()")
    public List<Product> getProductByPriceRange(BigDecimal min, BigDecimal max) {
        log.info("Finding all products within the given price range...");
        return productRepository.filterProducts(min, max);
    }


    public Product update(Long id, Product product) {
        Product prod = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        prod.setName(product.getName());
        prod.setPrice(product.getPrice());
        prod.setQuantity(product.getQuantity());
        prod.setDescription(product.getDescription());
        prod.setActive(product.getActive());

        Product updated = productRepository.save(prod);
        log.info("Product updated successfully");
        return updated;
    }

    public void softDelete(Long id) {
        Product prod = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        prod.setActive(false);
        productRepository.save(prod);
        log.info("Product deleted softly");
    }
}
