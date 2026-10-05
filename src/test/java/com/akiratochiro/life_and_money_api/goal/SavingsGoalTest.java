package com.akiratochiro.life_and_money_api.goal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SavingsGoalTest {

    private static SavingsGoal goalOf(String targetAmount, LocalDate deadline) {
        return new SavingsGoal(1L, "Notebook", new BigDecimal(targetAmount), deadline);
    }

    @Test
    void progressSplitsRemainingAmountByMonthsLeft() {
        // given
        SavingsGoal goal = goalOf("3000.00", LocalDate.of(2026, 12, 31));
        LocalDate today = LocalDate.of(2026, 9, 30);

        // when
        GoalProgress progress = goal.progress(new BigDecimal("500.00"), today);

        // then
        assertThat(progress.status()).isEqualTo(GoalStatus.IN_PROGRESS);
        assertThat(progress.monthsLeft()).isEqualTo(3);
        assertThat(progress.remaining()).isEqualByComparingTo("2500.00");
        assertThat(progress.monthlyNeeded()).isEqualByComparingTo("833.34");
    }

    @Test
    void goalIsReachedWhenSavedEqualsTarget() {
        // given
        SavingsGoal goal = goalOf("3000.00", LocalDate.of(2026, 12, 31));
        LocalDate today = LocalDate.of(2026, 9, 30);

        // when
        GoalProgress progress = goal.progress(new BigDecimal("3000.00"), today);

        // then
        assertThat(progress.status()).isEqualTo(GoalStatus.REACHED);
        assertThat(progress.remaining()).isEqualByComparingTo("0");
        assertThat(progress.monthlyNeeded()).isEqualByComparingTo("0");
    }

    @Test
    void goalIsOverdueWhenDeadlinePassedWithoutReachingTarget() {
        // given
        SavingsGoal goal = goalOf("3000.00", LocalDate.of(2026, 12, 31));
        LocalDate today = LocalDate.of(2027, 1, 1);

        // when
        GoalProgress progress = goal.progress(new BigDecimal("1000.00"), today);

        // then
        assertThat(progress.status()).isEqualTo(GoalStatus.OVERDUE);
        assertThat(progress.remaining()).isEqualByComparingTo("2000.00");
        assertThat(progress.monthlyNeeded()).isNull();
    }

    @Test
    void inTheDeadlineMonthTheWholeRemainingAmountIsNeeded() {
        // given
        SavingsGoal goal = goalOf("3000.00", LocalDate.of(2026, 12, 15));
        LocalDate today = LocalDate.of(2026, 12, 5);

        // when
        GoalProgress progress = goal.progress(new BigDecimal("2000.00"), today);

        // then
        assertThat(progress.status()).isEqualTo(GoalStatus.IN_PROGRESS);
        assertThat(progress.monthsLeft()).isZero();
        assertThat(progress.monthlyNeeded()).isEqualByComparingTo("1000.00");
    }

    @Test
    void remainingIsNeverNegativeWhenSavedExceedsTarget() {
        // given
        SavingsGoal goal = goalOf("3000.00", LocalDate.of(2026, 12, 31));
        LocalDate today = LocalDate.of(2026, 9, 30);

        // when
        GoalProgress progress = goal.progress(new BigDecimal("3500.00"), today);

        // then
        assertThat(progress.status()).isEqualTo(GoalStatus.REACHED);
        assertThat(progress.remaining()).isEqualByComparingTo("0");
    }
}