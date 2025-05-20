package com.ragnaralan.recipebook.mapper;

import com.ragnaralan.recipebook.model.Ingredient;
import com.ragnaralan.recipebook.model.dto.IngredientDto;
import com.ragnaralan.recipebook.model.dto.RecipeDto;
import com.ragnaralan.recipebook.model.dto.SimpleRecipeDto;
import com.ragnaralan.recipebook.model.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    IngredientDto toIngredientDto(Ingredient ingredient);

    RecipeDto toDto(Recipe recipe);

    SimpleRecipeDto toSimpleDto(Recipe recipe);
}
