package com.akiratochiro.life_and_money_api.transaction;

public interface GoalOwnershipChecker {
    boolean isOwnedBy(Long goalId, Long userId);
}
