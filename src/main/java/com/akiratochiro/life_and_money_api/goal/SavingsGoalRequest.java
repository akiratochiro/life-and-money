package com.akiratochiro.life_and_money_api.goal;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingsGoalRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Positive @Digits(integer = 17, fraction = 2)BigDecimal targetAmount,
        @NotNull LocalDate deadline
        ) {
}
