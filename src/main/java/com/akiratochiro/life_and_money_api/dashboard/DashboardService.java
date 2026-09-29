package com.akiratochiro.life_and_money_api.dashboard;

import com.akiratochiro.life_and_money_api.transaction.CategoryTotal;
import com.akiratochiro.life_and_money_api.transaction.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final TransactionService transactionService;

    public DashboardService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Transactional(readOnly = true)
    public ExpensesByCategoryResponse expensesByCategory(Long userId, YearMonth month) {
        List<CategoryTotal> totals = transactionService.sumExpensesByCategory(userId, month);

        BigDecimal total = totals.stream()
                .map(CategoryTotal::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CategoryExpense> categories = totals.stream()
                .map(t -> new CategoryExpense(
                        t.categoryId(), t.categoryName(), t.total(), percentageOf(t.total(), total)))
                .toList();

        return new ExpensesByCategoryResponse(month, total, categories);
    }

    private static BigDecimal percentageOf(BigDecimal part, BigDecimal whole) {
        if (whole.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return part.multiply(ONE_HUNDRED).divide(whole, 2, RoundingMode.HALF_UP);
    }
}
