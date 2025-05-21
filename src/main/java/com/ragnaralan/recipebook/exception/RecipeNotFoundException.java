package com.ragnaralan.recipebook.exception;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String recipeNotFound) {
        super(recipeNotFound);
    }
}
