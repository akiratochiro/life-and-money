package com.akiratochiro.life_and_money_api.goal;

import com.akiratochiro.life_and_money_api.transaction.GoalOwnershipChecker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RepositoryGoalOwnershipChecker implements GoalOwnershipChecker {

    private final SavingsGoalRepository savingsGoalRepository;

    RepositoryGoalOwnershipChecker(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnedBy(Long goalId, Long userId) {
        return savingsGoalRepository.existsByIdAndUserId(goalId, userId);
    }
}