package com.interview.assignment.domain.service.discount;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PremiumUserDiscount implements DiscountStrategy {

    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.9));
    }
}
