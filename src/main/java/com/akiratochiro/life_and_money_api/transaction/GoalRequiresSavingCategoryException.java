package com.akiratochiro.life_and_money_api.transaction;

public class GoalRequiresSavingCategoryException extends RuntimeException {
    public GoalRequiresSavingCategoryException(Long categoryId) {
        super("Your goal requires a category type Saving. Your category id: " + categoryId);
    }
}
