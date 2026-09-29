package com.akiratochiro.life_and_money_api.dashboard;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record ExpensesByCategoryResponse(
        YearMonth month,
        BigDecimal total,
        List<CategoryExpense> categories
) {
}
