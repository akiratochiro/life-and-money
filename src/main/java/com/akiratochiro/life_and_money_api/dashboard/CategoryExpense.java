package com.akiratochiro.life_and_money_api.dashboard;

import java.math.BigDecimal;

public record CategoryExpense(
        Long categoryId,
        String name,
        BigDecimal total,
        BigDecimal percentage
) {
}
