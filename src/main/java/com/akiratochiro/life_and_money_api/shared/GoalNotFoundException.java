package com.akiratochiro.life_and_money_api.shared;

public class GoalNotFoundException extends RuntimeException {
    public GoalNotFoundException(Long goalId) {
        super("Goal not Found, goalId: "+ goalId);
    }
}
