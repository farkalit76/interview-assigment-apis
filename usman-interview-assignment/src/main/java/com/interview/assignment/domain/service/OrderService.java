package com.interview.assignment.domain.service;

import com.interview.assignment.domain.dto.OrderItemRequest;
import com.interview.assignment.domain.dto.OrderRequest;
import com.interview.assignment.domain.exception.BusinessException;
import com.interview.assignment.domain.exception.ResourceNotFoundException;
import com.interview.assignment.domain.service.discount.DiscountFactory;
import com.interview.assignment.repository.OrderRepository;
import com.interview.assignment.repository.ProductRepository;
import com.interview.assignment.repository.entity.Order;
import com.interview.assignment.repository.entity.OrderItem;
import com.interview.assignment.repository.entity.Product;
import com.interview.assignment.repository.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DiscountFactory discountFactory;


    public Order placeOrder(User user, OrderRequest request) {

        List<OrderItem> items = new ArrayList<>();
        BigDecimal grossTotal = BigDecimal.ZERO;

        for (OrderItemRequest req : request.getItems()) {

            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for Id:"+req.getProductId()));

            //validate quantity in stock
            if (product.getQuantity() < req.getQuantity()) {
                throw new BusinessException("Insufficient stock for productId:"+product.getId());
            }

            //update quantity
            product.setQuantity(product.getQuantity() - req.getQuantity());

            //calculate total amount of the item
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity()));

            //Create Order Item
            OrderItem item = OrderItem.builder()
                    .productId(product.getId())
                    .quantity(req.getQuantity())
                    .unitPrice(product.getPrice())
                    .totalPrice(itemTotal)
                    .build();

            items.add(item);
            grossTotal  = grossTotal .add(itemTotal);
        }

        //Apply the discout based on User and amount
        BigDecimal finalTotal = discountFactory.calculate(user.getRole(), grossTotal );
        BigDecimal discountTotal = grossTotal.subtract(finalTotal);

        //create Order with total amount amd discount applied.
        Order order = Order.builder()
                .user(user)
                .orderTotal(finalTotal)
                .discountApplied(discountTotal)
                .build();

        items.forEach(itm -> itm.setOrder(order));
        order.setItems(items);

        return orderRepository.save(order);
    }
}
