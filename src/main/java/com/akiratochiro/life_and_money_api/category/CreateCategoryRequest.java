package com.akiratochiro.life_and_money_api.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(@NotBlank @Size(max=100) String name,
                                    @NotNull CategoryType type) {
}
