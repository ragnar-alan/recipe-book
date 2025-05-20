package com.ragnaralan.recipebook;

import org.springframework.boot.SpringApplication;

public class TestRecipeBookApplication {

	public static void main(String[] args) {
		SpringApplication.from(RecipeBookApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
