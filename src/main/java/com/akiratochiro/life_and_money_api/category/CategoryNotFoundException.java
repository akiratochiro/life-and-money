package com.akiratochiro.life_and_money_api.category;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category not found, id:" + id);
    }
}
