package com.akiratochiro.life_and_money_api.dashboard;

import com.akiratochiro.life_and_money_api.budget.BudgetItem;
import com.akiratochiro.life_and_money_api.budget.BudgetService;
import com.akiratochiro.life_and_money_api.category.Category;
import com.akiratochiro.life_and_money_api.category.CategoryService;
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
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final TransactionService transactionService;
    private final UserService userService;
    private final Clock clock;
    private final BudgetService budgetService;
    private final CategoryService categoryService;

    public DashboardService(TransactionService transactionService,
                            UserService userService,
                            Clock clock,
                            BudgetService budgetService,
                            CategoryService categoryService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.clock = clock;
        this.budgetService = budgetService;
        this.categoryService = categoryService;
    }

    // pizza graphic
    @Transactional(readOnly = true)
    public ExpensesByCategoryResponse expensesByCategory(Long userId, YearMonth month) {
        List<CategoryTotal> totals = transactionService.sumExpensesByCategory(userId, month);

        //total amount of the month
        BigDecimal total = totals.stream()
                .map(CategoryTotal::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // generates a new list of totals, adding percentage
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


    //histogram
    @Transactional(readOnly = true)
    public MonthlyTotalsResponse monthlyTotals(Long userId, int months) {
        YearMonth current = YearMonth.now(clock);
        YearMonth first = current.minusMonths(months - 1);

        //find and organize sums
        Map<YearMonth, Map<CategoryType, BigDecimal>> sums = new HashMap<>();
        for (MonthlyTypeTotal row : transactionService.sumByMonthAndType(userId, first, current)) {
            sums.computeIfAbsent(row.yearMonth(), m -> new EnumMap<>(CategoryType.class))
                    .put(row.type(), row.total());
        }

        //filling up gaps
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
        if (eligible.isEmpty()) {
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

    //forecasts and warnings
    private static final BigDecimal WARNING_THRESHOLD = new BigDecimal("0.80");

    @Transactional(readOnly = true)
    public BudgetDashboardResponse budget(Long userId, YearMonth month) {
        boolean closed = month.isBefore(YearMonth.now(clock));

        // sums of all income resources
        BigDecimal income = sumOf(transactionService.sumByCategory(userId, CategoryType.INCOME, month));

        //how much was spent for each category. It's a HashMap of ids and totals.
        Map<Long, BigDecimal> spentByCategory = new HashMap<>();
        for (CategoryType type : List.of(CategoryType.EXPENSE, CategoryType.SAVING)) {
            for (CategoryTotal total : transactionService.sumByCategory(userId, type, month)) {
                spentByCategory.put(total.categoryId(), total.total());
            }
        }

        // transforms a list into a map (categoryId and category)
        Map<Long, Category> categories = categoryService.list(userId, true).stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        //for each, calculates limit, lefts, percentage and status
        List<BudgetLine> lines = budgetService.listForMonth(userId, month).stream()
                .map(item -> toLine(
                        item,
                        categories.get(item.getCategoryId()),
                        income,
                        spentByCategory.getOrDefault(item.getCategoryId(), BigDecimal.ZERO)))
                .toList();

        return new BudgetDashboardResponse(month, closed, income, lines);
    }

    private static BudgetLine toLine(BudgetItem item, Category category,
                                     BigDecimal income, BigDecimal spent) {
        BigDecimal limit = item.effectiveLimit(income);
        BigDecimal remaining = limit.subtract(spent);
        BigDecimal usedPercentage = limit.signum() == 0 ? null : percentageOf(spent, limit);

        return new BudgetLine(
                category.getId(),
                category.getName(),
                category.getType(),
                item.getMode(),
                item.getLimitValue(),
                limit,
                spent,
                remaining,
                usedPercentage,
                statusOf(category.getType(), spent, limit)
        );
    }

    private static BigDecimal sumOf(List<CategoryTotal> totals) {
        return totals.stream()
                .map(CategoryTotal::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BudgetStatus statusOf(CategoryType type, BigDecimal spent, BigDecimal limit) {
        if (type == CategoryType.SAVING) {
            return spent.compareTo(limit) >= 0 ? BudgetStatus.REACHED : BudgetStatus.IN_PROGRESS;
        }
        if (limit.signum() == 0 && spent.signum() == 0) {
            return BudgetStatus.OK;
        }
        if (spent.compareTo(limit) > 0) {
            return BudgetStatus.EXCEEDED;
        }
        if (spent.compareTo(limit.multiply(WARNING_THRESHOLD)) >= 0) {
            return BudgetStatus.WARNING;
        }
        return BudgetStatus.OK;
    }
}
