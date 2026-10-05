package com.akiratochiro.life_and_money_api.goal;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "savings_goals")
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String name;
    private BigDecimal targetAmount;
    private LocalDate deadline;
    private Instant createdAt;

    protected SavingsGoal(){}

    public SavingsGoal(Long userId, String name, BigDecimal targetAmount, LocalDate deadline){
        this.userId = userId;
        this.name = normalizeName(name);
        this.targetAmount = targetAmount;
        this.deadline = deadline;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void update(String name, BigDecimal targetAmount, LocalDate deadline){
        this.name = normalizeName(name);
        this.targetAmount = targetAmount;
        this.deadline = deadline;
    }

    private static String normalizeName(String name){
        Objects.requireNonNull(name, "Goal name is required");
        return name.strip();
    }

    public GoalProgress progress(BigDecimal saved, LocalDate today) {
        BigDecimal remaining = targetAmount.subtract(saved).max(BigDecimal.ZERO);
        long monthsLeft = Math.max(0,
                ChronoUnit.MONTHS.between(YearMonth.from(today), YearMonth.from(deadline)));

        if (saved.compareTo(targetAmount) >= 0) {
            return new GoalProgress(saved, BigDecimal.ZERO, monthsLeft, BigDecimal.ZERO, GoalStatus.REACHED);
        }
        if (today.isAfter(deadline)) {
            return new GoalProgress(saved, remaining, 0, null, GoalStatus.OVERDUE);
        }

        BigDecimal monthlyNeeded = remaining.divide(
                BigDecimal.valueOf(Math.max(1, monthsLeft)), 2, RoundingMode.UP);
        return new GoalProgress(saved, remaining, monthsLeft, monthlyNeeded, GoalStatus.IN_PROGRESS);
    }
}