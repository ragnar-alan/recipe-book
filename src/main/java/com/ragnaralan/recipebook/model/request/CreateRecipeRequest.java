package com.ragnaralan.recipebook.model.request;

import com.ragnaralan.recipebook.model.MealType;
import com.ragnaralan.recipebook.model.dto.IngredientDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateRecipeRequest {
        @NotEmpty(message = "Name must not be empty")
        private String name;

        @NotEmpty(message = "Description must not be empty")
        private String description;

        @NotEmpty(message = "At least one ingredient must be provided")
        private List<IngredientDto> ingredients;

        @NotNull(message = "Cooking time must be provided")
        private Integer cookingTimeInMinutes;

        @NotNull(message = "Meal type must be provided")
        private MealType type;
}
