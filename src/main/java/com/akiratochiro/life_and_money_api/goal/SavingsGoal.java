package com.akiratochiro.life_and_money_api.goal;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
}