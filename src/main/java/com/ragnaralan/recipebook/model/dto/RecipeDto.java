package com.ragnaralan.recipebook.model.dto;

import com.ragnaralan.recipebook.model.MealType;

import java.util.List;

public record RecipeDto(
        Long id,
        String name,
        String description,
        List<IngredientDto> ingredients,
        Integer cookingTimeInMinutes,
        MealType type
) {
}
