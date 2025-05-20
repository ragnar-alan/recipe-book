package com.ragnaralan.recipebook.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    @GetMapping("/{recipeId}")
    public String getRecipeById() {
        return "Recipe details for the given ID";
    }

    @GetMapping("/list")
    public List<String> getRecipeList() {
        return List.of("Recipe 1", "Recipe 2", "Recipe 3");
    }

    @PostMapping
    public String createRecipe() {
        return "New recipe created";
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
