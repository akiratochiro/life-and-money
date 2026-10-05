package com.akiratochiro.life_and_money_api.goal;

import java.time.LocalDate;

public class InvalidGoalDeadlineException extends RuntimeException {
    public InvalidGoalDeadlineException(LocalDate deadline) {
        super("Deadline inválido, não pode ser anterior a data atual. Você escolheu: " + deadline);
    }
}
