package com.interview.assignment.domain.service.discount;

import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal applyDiscount(BigDecimal amount);
}
