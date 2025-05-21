package com.ragnaralan.recipebook.service;


import com.ragnaralan.recipebook.exception.RecipeNotFoundException;
import com.ragnaralan.recipebook.mapper.RecipeMapper;
import com.ragnaralan.recipebook.model.Ingredient;
import com.ragnaralan.recipebook.model.dto.IngredientDto;
import com.ragnaralan.recipebook.model.dto.RecipeDto;
import com.ragnaralan.recipebook.model.dto.SimpleRecipeDto;
import com.ragnaralan.recipebook.model.entity.Recipe;
import com.ragnaralan.recipebook.model.request.CreateRecipeRequest;
import com.ragnaralan.recipebook.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.ragnaralan.recipebook.model.MealType.BREAKFAST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void testGetRecipeShouldPass() {
        var expected = createMockRecipeDto();

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(createMockRecipe()));
        when(recipeMapper.toDto(any(Recipe.class))).thenReturn(expected);

        var actual = recipeService.getRecipe(100000L);

        assertSame(expected, actual);
    }

    @Test
    void testGetRecipe_missingRecipe() {
        var thrown = assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipe(100000L);
        }, "Recipe not found exception should be thrown");
        assertEquals("Recipe not found", thrown.getMessage());
    }

    @Test
    void testGetRecipesShouldPass() {
        var expected = List.of(createMockSimpleRecipeDto());

        when(recipeRepository.findAll()).thenReturn(List.of(createMockRecipe()));
        when(recipeMapper.toSimpleDtoList(anyList())).thenReturn(createMockSimpleRecipeDtosList());

        var actual = recipeService.getSimpleRecipeList();

        assertEquals(expected.getFirst().id(), actual.getFirst().id());
        assertEquals(expected.getFirst().name(), actual.getFirst().name());
        assertEquals(expected.getFirst().cookingTimeInMinutes(), actual.getFirst().cookingTimeInMinutes());
        assertEquals(expected.getFirst().type(), actual.getFirst().type());
    }

    @Test
    void testGetRecipesShouldPass_withNoRecipes() {
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());

        var actual = recipeService.getSimpleRecipeList();
        assertEquals(0, actual.size());
    }

    @Test
    void testCreateRecipe() {
        var mockRecipe = createMockRecipe();
        var mockRecipeDto = createMockRecipeDto();

        when(recipeMapper.toEntity(any(CreateRecipeRequest.class))).thenReturn(mockRecipe);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(mockRecipe);
        when(recipeMapper.toDto(any(Recipe.class))).thenReturn(mockRecipeDto);

        var mockRecipeRequest = new CreateRecipeRequest(
                "Mock Recipe",
                "Mock Description",
                List.of(new IngredientDto("Mock Ingredients", new BigDecimal("2"), "Mock Unit")),
                10,
                BREAKFAST
        );
        var actual = recipeService.createRecipe(mockRecipeRequest);
        var expected = createMockRecipeDto();
        //assertEquals(expected, actual);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testDeleteRecipe() {
        when(recipeRepository.existsById(anyLong())).thenReturn(false);

        var thrown = assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.deleteRecipe(100000L);
        }, "Recipe not found exception should be thrown");
        assertEquals("Recipe not found", thrown.getMessage());
    }

    @Test
    void testSearchForRecipes() {
        when(recipeRepository.searchForRecipes(anyString())).thenReturn(List.of(createMockRecipe()));
        when(recipeMapper.toSimpleDto(any(Recipe.class))).thenReturn(createMockSimpleRecipeDto());

        var actual = recipeService.searchForRecipes("Mock");
        var expected = createMockSimpleRecipeDtosList();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testSearchForRecipes_emptyList() {
        when(recipeRepository.searchForRecipes(anyString())).thenReturn(Collections.emptyList());

        var actual = recipeService.searchForRecipes("Mock");
        assertEquals(0, actual.size());
    }

    private Recipe createMockRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(100000L);
        recipe.setName("Mock Recipe");
        recipe.setDescription("Mock Description");
        recipe.setIngredients(List.of(new Ingredient("Mock Ingredients", new BigDecimal("2"), "Mock Unit")));
        recipe.setCookingTimeInMinutes(10);
        recipe.setType(BREAKFAST);
        return recipe;
    }

    private RecipeDto createMockRecipeDto() {
        return new RecipeDto(
                100000L,
                "Mock Recipe",
                "Mock Description",
                List.of(new IngredientDto("Mock Ingredients", new BigDecimal("2"), "Mock Unit")),
                10,
                BREAKFAST
        );
    }

    private SimpleRecipeDto createMockSimpleRecipeDto() {
        return new SimpleRecipeDto(
                100000L,
                "Mock Recipe",
                10,
                BREAKFAST
        );
    }

    private List<SimpleRecipeDto> createMockSimpleRecipeDtosList() {
        return List.of(new SimpleRecipeDto(
                100000L,
                "Mock Recipe",
                10,
                BREAKFAST
        ));
    }
}
