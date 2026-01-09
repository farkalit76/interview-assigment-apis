package com.interview.assignment.domain.service.discount;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserDiscount implements DiscountStrategy {

    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount;
    }
}
