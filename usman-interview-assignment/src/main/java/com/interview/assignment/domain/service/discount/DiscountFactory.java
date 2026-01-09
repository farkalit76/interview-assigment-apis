package com.interview.assignment.domain.service.discount;

import com.interview.assignment.domain.utils.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscountFactory {

    private final UserDiscount userDiscount;
    private final PremiumUserDiscount premiumUserDiscount;
    private final HighValueDiscount highValueDiscount;

    /**
     * Discount Rule
     * USER : no discount
     * PREMIUM_USER : 10% off total order
     * Orders > $500: extra 5% discount for any user
     *
     * @param role
     * @param amount
     * @return
     */
    public BigDecimal calculate(Role role, BigDecimal amount) {

        log.info("Discount applied for actual amount :{}  by user Role :{}", amount, role.name());
        BigDecimal discounted = switch (role) {
            case PREMIUM_USER -> premiumUserDiscount.applyDiscount(amount);
            default -> userDiscount.applyDiscount(amount);
        };
        log.info("Discount applied by User Role, Now Total Amount to pay is :{}", discounted);
        BigDecimal discountByTotalPurchase = highValueDiscount.apply(discounted);
        log.info("Next Discount applied by Total Amount Purchased, Now Total Amount to pay is :{}", discountByTotalPurchase);
        return discountByTotalPurchase;
    }
}
