package com.interview.assignment.domain.utils;


import com.interview.assignment.domain.dto.ProductResponse;
import com.interview.assignment.repository.entity.Product;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}
