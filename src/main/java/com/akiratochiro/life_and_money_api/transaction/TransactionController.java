package com.akiratochiro.life_and_money_api.transaction;

import com.akiratochiro.life_and_money_api.shared.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@AuthenticationPrincipal Jwt jwt,
                                      @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.create(CurrentUser.id(jwt),
                request.categoryId(), request.goalId(), request.amount(), request.transactionDate(), request.description());
        return TransactionResponse.from(transaction);
    }

    @GetMapping
    public List<TransactionResponse> list(@AuthenticationPrincipal Jwt jwt,
                                          @RequestParam YearMonth month) {
        return transactionService.list(CurrentUser.id(jwt), month).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@AuthenticationPrincipal Jwt jwt,
                                      @PathVariable Long id,
                                      @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.update(CurrentUser.id(jwt), id,
                request.categoryId(), request.goalId(), request.amount(), request.transactionDate(), request.description());
        return TransactionResponse.from(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        transactionService.delete(CurrentUser.id(jwt), id);
    }
}
