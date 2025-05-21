package com.ragnaralan.recipebook.mapper;

import com.ragnaralan.recipebook.model.Ingredient;
import com.ragnaralan.recipebook.model.dto.IngredientDto;
import com.ragnaralan.recipebook.model.dto.RecipeDto;
import com.ragnaralan.recipebook.model.dto.SimpleRecipeDto;
import com.ragnaralan.recipebook.model.entity.Recipe;
import com.ragnaralan.recipebook.model.request.CreateRecipeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    IngredientDto toIngredientDto(Ingredient ingredient);

    RecipeDto toDto(Recipe recipe);

    SimpleRecipeDto toSimpleDto(Recipe recipe);

    List<SimpleRecipeDto> toSimpleDtoList(List<Recipe> recipes);

    Recipe toEntity(CreateRecipeRequest request);
}
