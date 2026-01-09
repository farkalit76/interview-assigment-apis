package com.interview.assignment.domain.service.discount;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HighValueDiscount {

    public BigDecimal apply(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(500)) > 0) {
            return amount.multiply(BigDecimal.valueOf(0.95));
        }
        return amount;
    }
}
