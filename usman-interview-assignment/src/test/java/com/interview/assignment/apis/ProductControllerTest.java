package com.interview.assignment.apis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.assignment.domain.service.ProductService;
import com.interview.assignment.repository.entity.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) //disable header auth
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProducts() throws Exception {

        Product p1 = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(new BigDecimal("1000"))
                .quantity(5)
                .build();

        Product p2 = Product.builder()
                .id(2L)
                .name("Mouse")
                .price(new BigDecimal("50"))
                .quantity(20)
                .build();

        when(productService.getAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/v1/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].price").value(50));
    }

    @Test
    void shouldReturnAllProductsById() throws Exception {

        Product p1 = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(new BigDecimal("1000"))
                .quantity(5)
                .build();

        when(productService.getProductById(1L)).thenReturn(Optional.of(p1));

        mockMvc.perform(get("/v1/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void shouldReturnAllProductsByName() throws Exception {

        Product p1 = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(new BigDecimal("1000"))
                .quantity(5)
                .build();

        when(productService.getProductByName("Laptop")).thenReturn(List.of(p1));

        mockMvc.perform(get("/v1/api/products/name/Laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void shouldReturnProductsByPriceRange() throws Exception {

        Product p1 = Product.builder()
                .id(1L)
                .name("Keyboard")
                .price(new BigDecimal("200"))
                .quantity(10)
                .build();

        when(productService.getProductByPriceRange(
                new BigDecimal("100"),
                new BigDecimal("300")))
                .thenReturn(List.of(p1));

        mockMvc.perform(post("/v1/api/products/price/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "minPrice": 100,
                                      "maxPrice": 300
                                    }
                                """))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Keyboard"))
                .andExpect(jsonPath("$[0].price").value(200));
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsFound() throws Exception {

        when(productService.getProductByPriceRange(
                new BigDecimal("5000"),
                new BigDecimal("6000")))
                .thenReturn(List.of());

        mockMvc.perform(post("/v1/api/products/price/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "minPrice": 5000,
                                      "maxPrice": 6000
                                    }
                                """))
                .andExpect(jsonPath("$.size()").value(0));
    }
}
