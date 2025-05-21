package com.ragnaralan.recipebook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for serving web pages
 */
@Controller
public class WebController {

    /**
     * Serve the main page
     * @return the index template
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Serve the recipe detail page
     * @return the recipe-detail template
     */
    @GetMapping("/recipe-detail.html")
    public String recipeDetail() {
        return "recipe-detail";
    }

    /**
     * Serve the recipe form page
     * @return the recipe-form template
     */
    @GetMapping("/recipe-form.html")
    public String recipeForm() {
        return "recipe-form";
    }
}