/**
 * JavaScript file for the Recipe Detail page
 * Handles loading and displaying a single recipe
 */

// Define DTO-matching objects
/**
 * RecipeDto structure:
 * {
 *   id: number,
 *   name: string,
 *   description: string,
 *   ingredients: Array<IngredientDto>,
 *   cooking_time_in_minutes: number,
 *   type: string (BREAKFAST, LUNCH, DINNER)
 * }
 * 
 * IngredientDto structure:
 * {
 *   name: string,
 *   amount: number,
 *   unit: string
 * }
 */

$(document).ready(function() {
    // Get recipe ID from URL query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const recipeId = urlParams.get('id');
    
    if (!recipeId) {
        showError('Recipe ID is missing. Please go back to the recipe list.');
        return;
    }
    
    // Load recipe details
    loadRecipeDetails(recipeId);
});

/**
 * Load recipe details from the API
 * @param {number} recipeId - The ID of the recipe to load
 */
function loadRecipeDetails(recipeId) {
    $.ajax({
        url: `/api/v1/recipes/${recipeId}`,
        type: 'GET',
        dataType: 'json',
        success: function(recipe) {
            displayRecipeDetails(recipe);
            // Update page title
            document.title = `${recipe.name} - Recipe Details`;
        },
        error: function(xhr) {
            if (xhr.status === 404) {
                showError('Recipe not found. It may have been deleted.');
            } else {
                showError('Failed to load recipe details. Please try again later.');
            }
            console.error('Error loading recipe details:', xhr);
        }
    });
}

/**
 * Display recipe details in the UI
 * @param {RecipeDto} recipe - The recipe object
 */
function displayRecipeDetails(recipe) {
    // Get the template
    const template = document.getElementById('recipe-detail-template');
    const recipeDetail = document.importNode(template.content, true);
    
    // Set recipe data
    recipeDetail.querySelector('.recipe-name').textContent = recipe.name;
    recipeDetail.querySelector('.recipe-description').textContent = recipe.description;
    recipeDetail.querySelector('.cooking-time').textContent = recipe.cooking_time_in_minutes;
    
    // Set meal type
    const mealTypeElement = recipeDetail.querySelector('.meal-type');
    mealTypeElement.textContent = formatMealType(recipe.type);
    mealTypeElement.classList.add(recipe.type);
    
    // Add ingredients
    const ingredientsList = recipeDetail.querySelector('.ingredients-list');
    const ingredientTemplate = document.getElementById('ingredient-item-template');
    
    recipe.ingredients.forEach(ingredient => {
        const ingredientItem = document.importNode(ingredientTemplate.content, true);
        
        ingredientItem.querySelector('.ingredient-name').textContent = ingredient.name;
        ingredientItem.querySelector('.ingredient-amount').textContent = ingredient.amount;
        
        if (ingredient.unit) {
            ingredientItem.querySelector('.ingredient-unit').textContent = ingredient.unit;
        } else {
            ingredientItem.querySelector('.ingredient-unit').textContent = '';
        }
        
        ingredientsList.appendChild(ingredientItem);
    });
    
    // Clear loading spinner and append recipe details
    $('#recipeDetail').empty().append(recipeDetail);
}

/**
 * Format meal type for display
 * @param {string} mealType - The meal type (BREAKFAST, LUNCH, DINNER)
 * @returns {string} Formatted meal type
 */
function formatMealType(mealType) {
    return mealType.charAt(0) + mealType.slice(1).toLowerCase();
}

/**
 * Show error message
 * @param {string} message - The error message
 */
function showError(message) {
    const alertHtml = `<div class="alert alert-danger" role="alert">
        ${message}
    </div>`;
    
    $('#recipeDetail').html(alertHtml);
}