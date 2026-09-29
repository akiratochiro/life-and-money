package com.akiratochiro.life_and_money_api.budget;

public class IncomeCategoryBudgetException extends RuntimeException {
  public IncomeCategoryBudgetException(Long categoryId) {
    super("Category " + categoryId + " is an income category and cannot have a budget");
  }
}
