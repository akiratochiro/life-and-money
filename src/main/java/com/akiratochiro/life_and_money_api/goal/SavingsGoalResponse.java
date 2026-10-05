package com.akiratochiro.life_and_money_api.goal;


import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingsGoalResponse(
        Long id,
        String name,
        BigDecimal targetAmount,
        LocalDate deadline,
        BigDecimal saved,
        BigDecimal remaining,
        long monthsLeft,
        BigDecimal monthlyNeeded,
        GoalStatus status
) {
    public static SavingsGoalResponse from(SavingsGoal goal, GoalProgress progress) {
        return new SavingsGoalResponse(
                goal.getId(),
                goal.getName(),
                goal.getTargetAmount(),
                goal.getDeadline(),
                progress.saved(),
                progress.remaining(),
                progress.monthsLeft(),
                progress.monthlyNeeded(),
                progress.status()
        );
    }
}
