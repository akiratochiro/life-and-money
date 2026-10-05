package com.akiratochiro.life_and_money_api.budget;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BudgetItemTest {

    private static BudgetItem itemOf(BudgetMode mode, String limitValue, YearMonth validFrom) {
        return new BudgetItem(1L, 10L, mode, new BigDecimal(limitValue), validFrom);
    }

    @Test
    void amountModeIgnoresIncome() {
        // given
        BudgetItem item = itemOf(BudgetMode.AMOUNT, "1500.00", YearMonth.of(2026, 9));

        // when
        BigDecimal limit = item.effectiveLimit(new BigDecimal("6300.00"));

        // then
        assertThat(limit).isEqualByComparingTo("1500.00");
    }

    @Test
    void closingAnAlreadyClosedItemIsRejected() {
        // given
        BudgetItem item = itemOf(BudgetMode.AMOUNT, "1500.00", YearMonth.of(2026, 6));
        item.close(YearMonth.of(2026, 8));

        // when / then
        assertThatThrownBy(() -> item.close(YearMonth.of(2026, 9)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already closed");
    }

    @Test
    void percentageModeAppliesPercentageToIncome() {
        // given
        BudgetItem item = itemOf(BudgetMode.PERCENTAGE,"12",YearMonth.of(2026, 6));
        // when
        BigDecimal limit = item.effectiveLimit(new BigDecimal("6300.00"));
        // then
        assertThat(limit).isEqualByComparingTo("756.00");
    }


    @Test
    void closingBeforeStartMonthIsRejected() {
        // given
        BudgetItem item = itemOf(BudgetMode.AMOUNT, "1500.00", YearMonth.of(2026, 9));

        // when / then
        assertThatThrownBy(() -> item.close(YearMonth.of(2026, 8)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("before its start month");
    }

    @Test
    void closingSetsValidToAndDeactivatesItem() {
        // given
        BudgetItem item = itemOf(BudgetMode.AMOUNT, "1500.00", YearMonth.of(2026, 9));

        // when
        item.close(YearMonth.of(2026, 10));

        // then
        assertThat(item.getValidTo()).isEqualTo(YearMonth.of(2026, 10));
        assertThat(item.isActive()).isFalse();
    }

}


//Fechar corretamente: um item que começa em junho, fechado em agosto, deve ficar com o getValidTo() igual a agosto de 2026 e o isActive() igual a falso.
//Para comparar um YearMonth, o isEqualTo funciona normalmente: ele não tem o problema de escala do BigDecimal.
//        E, para um boolean, o AssertJ tem o isFalse().