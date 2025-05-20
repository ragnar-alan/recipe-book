package com.ragnaralan.recipebook.service;

import com.ragnaralan.recipebook.exception.RecipeNotFoundException;
import com.ragnaralan.recipebook.mapper.RecipeMapper;
import com.ragnaralan.recipebook.model.dto.RecipeDto;
import com.ragnaralan.recipebook.model.dto.SimpleRecipeDto;
import com.ragnaralan.recipebook.model.request.CreateRecipeRequest;
import com.ragnaralan.recipebook.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper mapper;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeDto getRecipe(Long id) {
        var recipe = recipeRepository.findById(id);
        return recipe
                .map(mapper::toDto)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
    }

    public List<SimpleRecipeDto> getSimpleRecipeList() {
        //@TODO add pagination if you have time for it
        // - https://howtodoinjava.com/spring-data/pagination-sorting-example/
        return recipeRepository.findAll()
                .stream()
                .map(mapper::toSimpleDto)
                .toList();
    }

    public RecipeDto createRecipe(CreateRecipeRequest request) {
        var recipeEntity = mapper.toEntity(request);
        return mapper.toDto(recipeRepository.save(recipeEntity));
    }

    public void deleteRecipe(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException("Recipe not found");
        }
        recipeRepository.deleteById(recipeId);
    }

    public List<SimpleRecipeDto> searchForRecipes(String searchExpression) {
        return recipeRepository.searchForRecipes(searchExpression)
                .stream().map(recipeMapper::toSimpleDto)
                .toList();
    }
}
