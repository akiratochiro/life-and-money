package com.akiratochiro.life_and_money_api.dashboard;

import java.util.List;

public record MonthlyTotalsResponse(
        List<MonthlyTotal> months,
        Averages averages
) {
}
