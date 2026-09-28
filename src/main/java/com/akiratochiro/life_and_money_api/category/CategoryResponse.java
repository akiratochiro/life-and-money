package com.akiratochiro.life_and_money_api.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CategoryResponse(Long id,
                               String name,
                               CategoryType type,
                               boolean archived,
                               Instant createdAt){
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.isArchived(),
                category.getCreatedAt()
        );
    }
}
