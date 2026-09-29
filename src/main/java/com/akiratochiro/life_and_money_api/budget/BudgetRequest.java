package com.akiratochiro.life_and_money_api.budget;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BudgetRequest(
        @NotNull BudgetMode mode,
        @NotNull @Positive @Digits(integer = 17, fraction = 2) BigDecimal limitValue
) {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @AssertTrue(message = "percentage must be at most 100")
    public boolean isPercentageWithinLimit() {
        if (mode != BudgetMode.PERCENTAGE || limitValue == null) {
            return true;
        }
        return limitValue.compareTo(ONE_HUNDRED) <= 0;
    }
}
