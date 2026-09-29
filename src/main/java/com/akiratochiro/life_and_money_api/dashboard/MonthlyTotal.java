package com.akiratochiro.life_and_money_api.dashboard;

import java.math.BigDecimal;
import java.time.YearMonth;

public record MonthlyTotal(
        YearMonth month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal saving,
        boolean closed
) {
}
