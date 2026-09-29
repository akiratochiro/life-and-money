package com.akiratochiro.life_and_money_api.budget;

import com.akiratochiro.life_and_money_api.shared.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public List<BudgetItemResponse> list(@AuthenticationPrincipal Jwt jwt,
                                         @RequestParam YearMonth month) {
        return budgetService.listForMonth(CurrentUser.id(jwt), month).stream()
                .map(BudgetItemResponse::from)
                .toList();
    }

    @PutMapping("/{categoryId}")
    public BudgetItemResponse set(@AuthenticationPrincipal Jwt jwt,
                                  @PathVariable Long categoryId,
                                  @Valid @RequestBody BudgetRequest request) {
        BudgetItem item = budgetService.setBudget(
                CurrentUser.id(jwt), categoryId, request.mode(), request.limitValue());
        return BudgetItemResponse.from(item);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@AuthenticationPrincipal Jwt jwt, @PathVariable Long categoryId) {
        budgetService.removeFromBudget(CurrentUser.id(jwt), categoryId);
    }
}
