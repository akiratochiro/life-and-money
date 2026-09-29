package com.akiratochiro.life_and_money_api.budget;

public class BudgetItemNotFoundException extends RuntimeException {
  public BudgetItemNotFoundException(Long categoryId) {
    super("No active budget item for category " + categoryId);
  }

}
