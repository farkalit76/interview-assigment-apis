package com.interview.assignment.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.assignment.domain.dto.OrderItemRequest;
import com.interview.assignment.domain.dto.OrderItemResponse;
import com.interview.assignment.domain.dto.OrderRequest;
import com.interview.assignment.domain.dto.OrderResponse;
import com.interview.assignment.domain.service.OrderService;
import com.interview.assignment.domain.service.UserService;
import com.interview.assignment.domain.utils.OrderMapper;
import com.interview.assignment.domain.utils.Role;
import com.interview.assignment.repository.entity.Order;
import com.interview.assignment.repository.entity.OrderItem;
import com.interview.assignment.repository.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) //disable header auth
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {

        // Request
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest request = OrderRequest.builder()
                .userId(1L)
                .items(List.of(itemRequest))
                .build();

        // User
        User user = User.builder()
                .id(1L)
                .username("test")
                .role(Role.USER)
                .build();

        // Order entity
        OrderItem item = OrderItem.builder()
                .productId(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100))
                .totalPrice(BigDecimal.valueOf(200))
                .build();

        Order order = Order.builder()
                .id(1L)
                .orderTotal(BigDecimal.valueOf(200))
                .items(List.of(item))
                .build();

        // Response DTO
        OrderItemResponse itemResponse = OrderItemResponse.builder()
                .productId(1L)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(100))
                .totalPrice(BigDecimal.valueOf(200))
                .build();

        OrderResponse response = OrderResponse.builder()
                .id(1L)
                .orderTotal(BigDecimal.valueOf(200))
                .items(List.of(itemResponse))
                .build();

        // Mocks
        when(userService.getByUerId(1L)).thenReturn(Optional.of(user));
        when(orderService.placeOrder(any(User.class), any(OrderRequest.class))).thenReturn(order);
        when(mapper.toResponse(order)).thenReturn(response);

        // Perform
        mockMvc.perform(post("/v1/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderTotal").value(200))
                .andExpect(jsonPath("$.items.size()").value(1))
                .andExpect(jsonPath("$.items[0].productId").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }
}
