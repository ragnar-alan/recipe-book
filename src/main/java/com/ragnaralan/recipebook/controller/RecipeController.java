package com.ragnaralan.recipebook.controller;

import com.ragnaralan.recipebook.model.dto.RecipeDto;
import com.ragnaralan.recipebook.model.dto.SimpleRecipeDto;
import com.ragnaralan.recipebook.model.request.CreateRecipeRequest;
import com.ragnaralan.recipebook.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipe API", description = "API for managing recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @Operation(summary = "Get a recipe by ID", description = "Returns a recipe based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipeById(
            @Parameter(description = "ID of the recipe to retrieve") @PathVariable Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipe(recipeId));
    }

    @Operation(summary = "Get a list of all recipes", description = "Returns a simplified list of all available recipes")
    @ApiResponse(responseCode = "200", description = "List of recipes retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleRecipeDto.class)))
    @GetMapping("/list")
    public ResponseEntity<List<SimpleRecipeDto>> getRecipeList() {
        return ResponseEntity.ok(recipeService.getSimpleRecipeList());
    }

    @Operation(summary = "Create a new recipe", description = "Creates a new recipe with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Void> createRecipe(
            @Parameter(description = "Recipe details", required = true) 
            @Valid @RequestBody CreateRecipeRequest request) {
        var recipe = recipeService.createRecipe(request);
        return ResponseEntity.created(
                URI.create("/api/v1/recipes/%s".formatted(recipe.id()))
        ).build();
    }

    @Operation(summary = "Delete a recipe", description = "Deletes a recipe based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(description = "ID of the recipe to delete") @PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search for recipes", description = "Searches for recipes based on the provided search expression")
    @ApiResponse(responseCode = "200", description = "Search results",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleRecipeDto.class)))
    @GetMapping("/search")
    public ResponseEntity<List<SimpleRecipeDto>> searchForRecipes(
            @Parameter(description = "Search expression to filter recipes", required = true)
            @RequestParam String searchExpression) {
        var recipes = recipeService.searchForRecipes(searchExpression);
        return ResponseEntity.ok(recipes);
    }

    // This endpoint made only for testing purposes
    @Operation(summary = "Bulk create recipes - for testing purposes onlyCheckstyle", description = "Creates multiple recipes at once (for testing purposes only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipes created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/bulk-create-recipes")
    public ResponseEntity<Void> createBulkRecipes(
            @Parameter(description = "List of recipe details", required = true)
            @RequestBody List<CreateRecipeRequest> requests) {
        for (CreateRecipeRequest request : requests) {
            recipeService.createRecipe(request);
        }
        return ResponseEntity.created(URI.create("/api/v1/recipes/bulk-create")).build();
    }

}
