package com.ragnaralan.recipebook.repository;

import com.ragnaralan.recipebook.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
