package com.interview.assignment.apis;


import com.interview.assignment.domain.dto.OrderRequest;
import com.interview.assignment.domain.dto.OrderResponse;
import com.interview.assignment.domain.exception.ResourceNotFoundException;
import com.interview.assignment.domain.service.OrderService;
import com.interview.assignment.domain.service.UserService;
import com.interview.assignment.domain.utils.OrderMapper;
import com.interview.assignment.repository.entity.Order;
import com.interview.assignment.repository.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/v1/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order API")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final OrderMapper mapper;

    @PostMapping
    @Operation(summary = "Place an order", description = "Place order for a user with product id and quantity.")
    //@PreAuthorize("hasAnyRole('USER','PREMIUM_USER')")
    //public Order placeOrder(@RequestBody List<OrderItem> items, @AuthenticationPrincipal User user) {
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {

        //User user = User.builder().id(1L).username("test").password("tesT@22!").role(Role.ADMIN).build(); //default User
        User user = userService.getByUerId(request.getUserId()).orElseThrow( () -> new ResourceNotFoundException("User not found."));
        log.info("USer name found :{}", user.getUsername());
        Order order = orderService.placeOrder(user, request);

        return mapper.toResponse(order);
    }
}
