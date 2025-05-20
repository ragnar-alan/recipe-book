package com.ragnaralan.recipebook.model.dto;

public record IngredientDto(
        String name,
        Integer amount,
        String unit
) {
}
