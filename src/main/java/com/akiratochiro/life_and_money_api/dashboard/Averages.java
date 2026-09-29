package com.akiratochiro.life_and_money_api.dashboard;

import java.math.BigDecimal;

public record Averages(
        BigDecimal income,
        BigDecimal expense,
        BigDecimal saving,
        int basedOnMonths
        ) {
}
