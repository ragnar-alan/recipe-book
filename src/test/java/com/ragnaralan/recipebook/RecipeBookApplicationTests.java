package com.ragnaralan.recipebook;

import com.ragnaralan.recipebook.repository.RecipeRepository;
import com.ragnaralan.recipebook.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RecipeBookApplicationTests {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private RecipeRepository recipeRepository;

	@Test
	void contextLoads() {
		// Verify that the application context loads successfully
		// by checking that key components are properly initialized
		assertNotNull(recipeService, "RecipeService should be initialized");
		assertNotNull(recipeRepository, "RecipeRepository should be initialized");
	}

}
