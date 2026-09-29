package com.akiratochiro.life_and_money_api.budget;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "budget_items")
public class BudgetItem {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private BudgetMode mode;

    private BigDecimal limitValue;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Instant createdAt;

    protected BudgetItem() {
    }

    public BudgetItem(Long userId, Long categoryId, BudgetMode mode,
                      BigDecimal limitValue, YearMonth validFrom) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.mode = mode;
        this.limitValue = limitValue;
        this.validFrom = validFrom.atDay(1);
        this.validTo = null;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public BudgetMode getMode() {
        return mode;
    }

    public BigDecimal getLimitValue() {
        return limitValue;
    }

    public YearMonth getValidFrom() {
        return YearMonth.from(validFrom);
    }

    public YearMonth getValidTo() {
        if (validTo == null) {
            return null;
        }
        return YearMonth.from(validTo);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return validTo == null;
    }

    public void close(YearMonth lastMonth) {
        if (!isActive()) {
            throw new IllegalStateException("Budget item " + id + " is already closed");
        }
        if (lastMonth.isBefore(getValidFrom())) {
            throw new IllegalArgumentException(
                    "Cannot close budget item before its start month " + getValidFrom());
        }
        this.validTo = lastMonth.atDay(1);
    }

    public BigDecimal effectiveLimit(BigDecimal monthlyIncome) {
        if (mode == BudgetMode.AMOUNT) {
            return limitValue;
        }
        return monthlyIncome
                .multiply(limitValue)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }
}