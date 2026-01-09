package com.interview.assignment.repository;

import com.interview.assignment.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    Optional<Product> findByIdAndActiveTrue(Long id);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true
        AND p.price BETWEEN :min AND :max
        AND p.quantity > 0
    """)
    List<Product> filterProducts(BigDecimal min, BigDecimal max);
}
