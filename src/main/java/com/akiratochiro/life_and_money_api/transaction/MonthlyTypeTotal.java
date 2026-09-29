package com.akiratochiro.life_and_money_api.transaction;

import com.akiratochiro.life_and_money_api.category.CategoryType;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyTypeTotal(Integer year, Integer month, CategoryType type, BigDecimal total) {
    public YearMonth yearMonth() {
        return YearMonth.of(year, month);
    }
}
