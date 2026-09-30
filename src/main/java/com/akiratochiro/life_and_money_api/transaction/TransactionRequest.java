package com.akiratochiro.life_and_money_api.transaction;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull Long categoryId,
        Long goalId,
        @NotNull @Positive @Digits(integer = 17, fraction = 2) BigDecimal amount,
        @NotNull LocalDate transactionDate,
        @Size(max = 255) String description
        ) {
}
