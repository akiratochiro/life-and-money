package com.akiratochiro.life_and_money_api.transaction;

import com.akiratochiro.life_and_money_api.category.Category;
import com.akiratochiro.life_and_money_api.category.CategoryResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        Long categoryId,
        Long goalId,
        BigDecimal amount,
        LocalDate transactionDate,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getCategoryId(),
                transaction.getGoalId(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
