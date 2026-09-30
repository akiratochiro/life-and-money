package com.akiratochiro.life_and_money_api.dashboard;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record BudgetDashboardResponse(
        YearMonth month,
        boolean closed,
        BigDecimal income,
        List<BudgetLine> items
) {
}
