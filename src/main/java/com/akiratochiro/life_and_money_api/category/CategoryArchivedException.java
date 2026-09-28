package com.akiratochiro.life_and_money_api.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class CategoryArchivedException extends RuntimeException {
    public CategoryArchivedException(Long id) {
      super("Category " + id + " is archived and cannot receive new transactions");
    }
}
