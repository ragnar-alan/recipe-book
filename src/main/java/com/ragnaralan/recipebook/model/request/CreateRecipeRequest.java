package com.ragnaralan.recipebook.model.request;

import com.ragnaralan.recipebook.model.MealType;
import com.ragnaralan.recipebook.model.dto.IngredientDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateRecipeRequest(
        @NotEmpty(message = "Name must not be empty")
        String name,

        @NotEmpty(message = "Description must not be empty")
        String description,

        @NotEmpty(message = "At least one ingredient must be provided")
        List<IngredientDto> ingredients,

        @NotNull(message = "Cooking time must be provided")
        Integer cookingTimeInMinutes,

        @NotNull(message = "Meal type must be provided")
        MealType type
) { }
