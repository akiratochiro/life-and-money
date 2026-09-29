package com.akiratochiro.life_and_money_api.budget;

import java.math.BigDecimal;
import java.time.YearMonth;

public record BudgetItemResponse(
        Long id,
        Long categoryId,
        BudgetMode mode,
        BigDecimal limitValue,
        YearMonth validFrom,
        YearMonth validTo,
        boolean active
) {

    public static BudgetItemResponse from(BudgetItem budgetItem) {
        return new BudgetItemResponse(
                budgetItem.getId(),
                budgetItem.getCategoryId(),
                budgetItem.getMode(),
                budgetItem.getLimitValue(),
                budgetItem.getValidFrom(),
                budgetItem.getValidTo(),
                budgetItem.isActive()
        );
    }
}