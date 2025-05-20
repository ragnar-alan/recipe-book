package com.ragnaralan.recipebook.repository;

import com.ragnaralan.recipebook.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {


    @Query(
            value = "SELECT * FROM recipes WHERE to_tsvector('english', name || ' ' || ingredients) @@ to_tsquery(:searchExpression)",
            nativeQuery = true
    )
    List<Recipe> searchForRecipes(@Param("searchExpression") String searchExpression);
}
