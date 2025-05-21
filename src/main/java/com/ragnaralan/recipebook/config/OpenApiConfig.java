package com.ragnaralan.recipebook.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI recipeBookOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Recipe Book API")
                        .description("Spring Boot REST API for managing recipes")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Recipe Book Team")
                                .email("support@recipebook.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}