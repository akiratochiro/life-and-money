package com.akiratochiro.life_and_money_api.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameCategoryRequest(@NotBlank @Size(max=100) String name) {
}
