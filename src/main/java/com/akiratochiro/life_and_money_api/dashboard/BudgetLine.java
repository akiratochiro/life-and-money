package com.akiratochiro.life_and_money_api.dashboard;

import com.akiratochiro.life_and_money_api.budget.BudgetMode;
import com.akiratochiro.life_and_money_api.category.CategoryType;

import java.math.BigDecimal;

public record BudgetLine(
        Long categoryId,
        String categoryName,
        CategoryType categoryType,
        BudgetMode mode,
        BigDecimal limitValue,
        BigDecimal effectiveLimit,
        BigDecimal spent,
        BigDecimal remaining,
        BigDecimal usedPercentage,
        BudgetStatus status
) {
}
