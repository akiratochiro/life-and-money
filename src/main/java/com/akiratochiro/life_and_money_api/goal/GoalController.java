package com.akiratochiro.life_and_money_api.goal;


import com.akiratochiro.life_and_money_api.shared.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService){
        this.goalService = goalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsGoalResponse create(@AuthenticationPrincipal Jwt jwt,
                                      @Valid @RequestBody SavingsGoalRequest request){
        return goalService.create(CurrentUser.id(jwt),
                request.name(), request.targetAmount(), request.deadline());
    }

    @GetMapping
    public List<SavingsGoalResponse> list(@AuthenticationPrincipal Jwt jwt
                                         ){
        return goalService.list(CurrentUser.id(jwt));
    }

    @PutMapping("/{id}")
    public SavingsGoalResponse update(@AuthenticationPrincipal Jwt jwt,
                                      @PathVariable Long id,
                                      @Valid @RequestBody SavingsGoalRequest request){
        return goalService.update(CurrentUser.id(jwt), id,
                request.name(), request.targetAmount(), request.deadline());
    }

    @DeleteMapping("/{id}")
    public GoalDeletionResponse delete(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable Long id) {
        return goalService.delete(CurrentUser.id(jwt), id);
    }
}
