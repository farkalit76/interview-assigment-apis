package com.interview.assignment.apis;

import com.interview.assignment.domain.dto.ProductRequest;
import com.interview.assignment.domain.dto.ProductResponse;
import com.interview.assignment.domain.dto.ProductSearchRequest;
import com.interview.assignment.domain.exception.ResourceNotFoundException;
import com.interview.assignment.domain.service.ProductService;
import com.interview.assignment.domain.utils.ProductMapper;
import com.interview.assignment.repository.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Create product", description = "Create product with its price and quantity")
    //@PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @GetMapping
    @Operation(summary = "Fetch all product", description = "Fetch all product details.")
    public List<ProductResponse> list() {
        List<Product> products = productService.getAll();

        return products.stream()
                        .map(ProductMapper::toResponse)
                        .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch product by id", description = "Fetch product details.")
    public ProductResponse findById(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Fetch product by Name", description = "Fetch all product details.")
    public List<ProductResponse> getProductByName(@PathVariable String name) {
        List<Product> products = productService.getProductByName(name);
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @PostMapping("/price/range")
    @Operation(summary = "Fetch product by price range", description = "Fetch all product details.")
    public List<ProductResponse> getProductByPriceRange(@RequestBody @Validated ProductSearchRequest request) {
        List<Product> products = productService.getProductByPriceRange(request.getMinPrice(), request.getMaxPrice());
        log.info("products by price range :{}", products.size());
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by Id", description = "Update product detail by id.")
    //@PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by id", description = "Delete a product detail by Id.")
    //@PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        productService.softDelete(id);
        return "Deleted item :"+id;
    }
}
