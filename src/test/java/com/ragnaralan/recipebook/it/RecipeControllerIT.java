package com.ragnaralan.recipebook.it;


import com.ragnaralan.recipebook.model.dto.IngredientDto;
import com.ragnaralan.recipebook.model.request.CreateRecipeRequest;
import io.restassured.RestAssured;
import org.approvaltests.JsonApprovals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.ragnaralan.recipebook.model.MealType.BREAKFAST;

@Testcontainers
@Sql("/test-data.sql")
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerIT {
    private static final DockerImageName IMAGE_NAME = DockerImageName
            .parse("postgres:16-alpine")
            .asCompatibleSubstituteFor("postgres");
    @Container
    static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(IMAGE_NAME)
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @LocalServerPort
    private int port;

    static {
        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @ParameterizedTest
    @MethodSource("createRecipeTestCases")
    @DisplayName("Create recipe should fail due to {1}")
    void testRecipeCreationShouldFail_dueTo(CreateRecipeRequest request, String issue) throws JsonProcessingException {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        var result = RestAssured
                .given()
                    .contentType("application/json")
                    .body(objectMapper.writeValueAsString(request))
                .when()
                    .post("/api/v1/recipes")
                .then()
                    .statusCode(400)
                .extract()
                .asString();
        JsonApprovals.verifyJson(result,
                new Options()
                        .forFile()
                        .withBaseName("testRecipeCreationShouldFail_dueTo_%s".formatted(issue)));
    }

    @Test
    void testGetRecipeShouldPass() {
        var result = RestAssured
                .given()
                    .contentType("application/json")
                .when()
                    .get("/api/v1/recipes/100000")
                .then()
                    .statusCode(200)
                .extract()
                .asString();
        JsonApprovals.verifyJson(result, new Options().forFile().withBaseName("testGetRecipeShouldPass"));
    }

    @Test
    void testGetRecipeShouldFail_dueToMissingRecipe() {
        var result = RestAssured
                .given()
                    .contentType("application/json")
                .when()
                    .get("/api/v1/recipes/468545614")
                .then()
                    .statusCode(404)
                    .extract()
                    .asString();
        JsonApprovals.verifyJson(result, new Options().forFile().withBaseName("testGetRecipeShouldFail_dueToMissingRecipe"));
    }

    @Test
    void testGetRecipeListShouldPass() {
        var result = RestAssured
                .given()
                .when()
                    .get("/api/v1/recipes/list")
                .then()
                    .statusCode(200)
                .extract()
                .asString();
        JsonApprovals.verifyJson(result, new Options().forFile().withBaseName("testGetRecipeListShouldPass"));
    }

    @Test
    void testDeleteRecipeShouldPass() {
        RestAssured
                .given()
                .when()
                    .delete("/api/v1/recipes/100018")
                .then()
                    .statusCode(204);
        var result = RestAssured
                .given()
                .when()
                    .get("/api/v1/recipes/100018")
                .then()
                    .statusCode(404)
                    .extract()
                    .asString();
        JsonApprovals.verifyJson(result, new Options().forFile().withBaseName("testDeleteRecipeShouldPass"));
    }

    @Test
    void testSearchForRecipeShouldPass() {
        var result = RestAssured
                .given()
                    .queryParam("searchExpression", "salad")
                .when()
                    .get("/api/v1/recipes/search")
                .then()
                    .statusCode(200)
                .extract()
                .asString();
        JsonApprovals.verifyJson(result, new Options().forFile().withBaseName("testSearchForRecipeShouldPass"));
    }


    private static Stream<Arguments> createRecipeTestCases() {
        return Stream.of(
                Arguments.of(new CreateRecipeRequest(null, "Mock Description", List.of(new IngredientDto("Mock Ingredients", new BigDecimal("2"), "Mock Unit")), 10, BREAKFAST), "empty_name"),
                Arguments.of(new CreateRecipeRequest("Mock Recipe", null, List.of(new IngredientDto("Mock Ingredients", new BigDecimal("2"), "Mock Unit")), 10, BREAKFAST), "empty_description"),
                Arguments.of(new CreateRecipeRequest("Mock Recipe", "Mock Description", Collections.emptyList(), 10, BREAKFAST), "empty_ingredients"),
                Arguments.of(new CreateRecipeRequest("Mock Recipe", "Mock Description", List.of(new IngredientDto("Mock Ingredients", new BigDecimal("2"), "Mock Unit")), null, BREAKFAST), "empty_cooking_time"),
                Arguments.of(new CreateRecipeRequest("Mock Recipe", "Mock Description", List.of(new IngredientDto("Mock Ingredients", new BigDecimal("2"), "Mock Unit")), 10, null), "empty_meal_type")
        );
    }
}
