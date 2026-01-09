package com.interview.assignment.domain.utils;


import com.interview.assignment.domain.dto.OrderItemResponse;
import com.interview.assignment.domain.dto.OrderResponse;
import com.interview.assignment.repository.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setOrderTotal(order.getOrderTotal());
        orderResponse.setDiscountApplied(order.getDiscountApplied());
        orderResponse.setUserId(order.getUser().getId());

        List<OrderItemResponse> items = order.getItems().stream().map(item -> {
            OrderItemResponse orderItem = new OrderItemResponse();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItem.setTotalPrice(item.getTotalPrice());
            return orderItem;
        }).toList();

        orderResponse.setItems(items);
        return orderResponse;
    }

}
