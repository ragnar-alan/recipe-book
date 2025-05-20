package com.ragnaralan.recipebook.controller;

import com.ragnaralan.recipebook.model.dto.RecipeDto;
import com.ragnaralan.recipebook.model.dto.SimpleRecipeDto;
import com.ragnaralan.recipebook.model.request.CreateRecipeRequest;
import com.ragnaralan.recipebook.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipe(recipeId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<SimpleRecipeDto>> getRecipeList() {
        return ResponseEntity.ok(recipeService.getSimpleRecipeList());
    }

    @PostMapping
    public ResponseEntity<Void> createRecipe(@Valid @RequestBody CreateRecipeRequest request) {
        var recipe = recipeService.createRecipe(request);
        return ResponseEntity.created(
                URI.create("/api/v1/recipes/%s".formatted(recipe.id()))
        ).build();
    }

    @DeleteMapping("/{recipeId}")
    public String deleteRecipe() {
        return "Recipe deleted";
    }

    @PostMapping("/search")
    public List<String> searchRecipe() {
        return List.of("Search results for recipes");
    }

}
