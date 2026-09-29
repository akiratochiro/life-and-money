package com.akiratochiro.life_and_money_api.dashboard;


import com.akiratochiro.life_and_money_api.budget.BudgetItemResponse;
import com.akiratochiro.life_and_money_api.shared.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @GetMapping("/expenses-by-category")
    public ExpensesByCategoryResponse expensesByCategory(@AuthenticationPrincipal Jwt jwt,
                                        @RequestParam YearMonth month){

        return dashboardService.expensesByCategory(CurrentUser.id(jwt), month);
    }
}
