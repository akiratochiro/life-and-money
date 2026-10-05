package com.akiratochiro.life_and_money_api.goal;

import java.math.BigDecimal;

public record GoalProgress(
        BigDecimal saved,
        BigDecimal remaining,
        long monthsLeft,
        BigDecimal monthlyNeeded,
        GoalStatus status
) {
}
