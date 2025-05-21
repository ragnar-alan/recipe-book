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

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe
     * @return the recipe as a {@link RecipeDto}
     * @throws RecipeNotFoundException if the recipe is not found
     */
    public RecipeDto getRecipe(Long id) {
        var recipe = recipeRepository.findById(id);
        var result = recipe
                .map(recipeItem -> mapper.toDto(recipeItem))
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        return result;
    }

    /**
     * Retrieves a list of all recipes in a simplified form. If there is no recipe it will return an empty list.
     *
     * @return a list of {@link SimpleRecipeDto}
     */
    public List<SimpleRecipeDto> getSimpleRecipeList() {
        //@TODO add pagination if you have time for it
        // - https://howtodoinjava.com/spring-data/pagination-sorting-example/
        var recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            return List.of();
        }
        return recipeMapper.toSimpleDtoList(recipes);
    }

    /**
     * Creates a new recipe from the provided request.
     *
     * @param request the recipe creation request. Every field is mandatory and list should have to contain at least one item
     * @return the created recipe as a {@link RecipeDto}
     */
    public RecipeDto createRecipe(CreateRecipeRequest request) {
        var recipeEntity = mapper.toEntity(request);
        return mapper.toDto(recipeRepository.save(recipeEntity));
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param recipeId the ID of the recipe to delete
     * @throws RecipeNotFoundException if the recipe is not found
     */
    public void deleteRecipe(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new RecipeNotFoundException("Recipe not found");
        }
        recipeRepository.deleteById(recipeId);
    }

    /**
     * Searches for recipes matching the given search expression.
     *
     * @param searchExpression the search expression to filter recipes
     * @return a list of {@link SimpleRecipeDto} matching the search criteria
     */
    public List<SimpleRecipeDto> searchForRecipes(String searchExpression) {
        return recipeRepository.searchForRecipes(searchExpression)
                .stream().map(recipeMapper::toSimpleDto)
                .toList();
    }
}
