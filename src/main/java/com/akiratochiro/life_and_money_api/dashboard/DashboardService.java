package com.akiratochiro.life_and_money_api.dashboard;

import com.akiratochiro.life_and_money_api.category.CategoryType;
import com.akiratochiro.life_and_money_api.transaction.CategoryTotal;
import com.akiratochiro.life_and_money_api.transaction.MonthlyTypeTotal;
import com.akiratochiro.life_and_money_api.transaction.TransactionService;
import com.akiratochiro.life_and_money_api.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;

@Service
public class DashboardService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final TransactionService transactionService;
    private final UserService userService;
    private final Clock clock;

    public DashboardService(TransactionService transactionService,
                            UserService userService,
                            Clock clock) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.clock = clock;
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

    @Transactional(readOnly = true)
    public MonthlyTotalsResponse monthlyTotals(Long userId, int months) {
        YearMonth current = YearMonth.now(clock);
        YearMonth first = current.minusMonths(months - 1);

        Map<YearMonth, Map<CategoryType, BigDecimal>> sums = new HashMap<>();
        for (MonthlyTypeTotal row : transactionService.sumByMonthAndType(userId, first, current)) {
            sums.computeIfAbsent(row.yearMonth(), m -> new EnumMap<>(CategoryType.class))
                    .put(row.type(), row.total());
        }

        List<MonthlyTotal> result = new ArrayList<>();
        for (YearMonth month = first; !month.isAfter(current); month = month.plusMonths(1)) {
            Map<CategoryType, BigDecimal> totals = sums.getOrDefault(month, Map.of());
            result.add(new MonthlyTotal(
                    month,
                    totals.getOrDefault(CategoryType.INCOME, BigDecimal.ZERO),
                    totals.getOrDefault(CategoryType.EXPENSE, BigDecimal.ZERO),
                    totals.getOrDefault(CategoryType.SAVING, BigDecimal.ZERO),
                    month.isBefore(current)));
        }

        YearMonth registrationMonth = YearMonth.from(
                userService.findById(userId).getCreatedAt().atZone(clock.getZone()));

        List<MonthlyTotal> eligible = result.stream()
                .filter(MonthlyTotal::closed)
                .filter(total -> !total.month().isBefore(registrationMonth))
                .toList();

        return new MonthlyTotalsResponse(result, averagesOf(eligible));
    }

    private static Averages averagesOf(List<MonthlyTotal> eligible) {
        if(eligible.isEmpty()){
            return null;
        }
        return new Averages(
                averageOf(eligible, MonthlyTotal::income),
                averageOf(eligible, MonthlyTotal::expense),
                averageOf(eligible, MonthlyTotal::saving),
                eligible.size()
        );
    }

    private static BigDecimal averageOf(List<MonthlyTotal> totals,
                                        Function<MonthlyTotal, BigDecimal> field) {
        BigDecimal sum = totals.stream()
                .map(field)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(totals.size()), 2, RoundingMode.HALF_UP);
    }
}
