package com.ragnaralan.recipebook.model.dto;

import com.ragnaralan.recipebook.model.MealType;

public record SimpleRecipeDto(
        Long id,
        String name,
        Integer cookingTimeInMinutes,
        MealType type
) {
}
