package com.akiratochiro.life_and_money_api.goal;


import java.math.BigDecimal;

public record GoalDeletionResponse(
        Long goalId,
        BigDecimal keptAsSavings
) {
}
