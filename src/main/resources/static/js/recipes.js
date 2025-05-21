/**
 * Main JavaScript file for the Recipe Book index page
 * Handles listing recipes and search functionality
 */

// Define DTO-matching objects
/**
 * SimpleRecipeDto structure:
 * {
 *   id: number,
 *   name: string,
 *   cooking_time_in_minutes: number,
 *   type: string (BREAKFAST, LUNCH, DINNER)
 * }
 */

$(document).ready(function() {
    // Load all recipes when page loads
    loadRecipes();
    
    // Set up search button click handler
    $('#searchButton').click(function() {
        const searchTerm = $('#searchInput').val().trim();
        if (searchTerm) {
            searchRecipes(searchTerm);
        } else {
            loadRecipes();
        }
    });
    
    // Set up search input enter key handler
    $('#searchInput').keypress(function(e) {
        if (e.which === 13) { // Enter key
            $('#searchButton').click();
            return false;
        }
    });
    
    // Event delegation for dynamically created elements
    $('#recipeList').on('click', '.view-recipe', function(e) {
        e.preventDefault();
        const recipeId = $(this).data('id');
        window.location.href = `/recipe-detail.html?id=${recipeId}`;
    });
    
    $('#recipeList').on('click', '.delete-recipe', function() {
        const recipeId = $(this).data('id');
        const recipeName = $(this).closest('.card').find('.recipe-name').text();
        
        if (confirm(`Are you sure you want to delete "${recipeName}"?`)) {
            deleteRecipe(recipeId);
        }
    });
});

/**
 * Load all recipes from the API
 */
function loadRecipes() {
    $('#recipeList').html('<div class="col-12 text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>');
    
    $.ajax({
        url: '/api/v1/recipes/list',
        type: 'GET',
        dataType: 'json',
        success: function(recipes) {
            displayRecipes(recipes);
        },
        error: function(xhr) {
            showError('Failed to load recipes. Please try again later.');
            console.error('Error loading recipes:', xhr);
        }
    });
}

/**
 * Search for recipes using the API
 * @param {string} searchTerm - The term to search for
 */
function searchRecipes(searchTerm) {
    $('#recipeList').html('<div class="col-12 text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>');
    
    $.ajax({
        url: `/api/v1/recipes/search?searchExpression=${encodeURIComponent(searchTerm)}`,
        type: 'GET',
        dataType: 'json',
        success: function(recipes) {
            displayRecipes(recipes);
            if (recipes.length === 0) {
                $('#recipeList').append('<div class="col-12"><div class="alert alert-info">No recipes found matching your search.</div></div>');
            }
        },
        error: function(xhr) {
            showError('Failed to search recipes. Please try again later.');
            console.error('Error searching recipes:', xhr);
        }
    });
}

/**
 * Delete a recipe using the API
 * @param {number} recipeId - The ID of the recipe to delete
 */
function deleteRecipe(recipeId) {
    $.ajax({
        url: `/api/v1/recipes/${recipeId}`,
        type: 'DELETE',
        success: function() {
            // Reload the recipes after successful deletion
            loadRecipes();
            showSuccess('Recipe deleted successfully!');
        },
        error: function(xhr) {
            showError('Failed to delete recipe. Please try again later.');
            console.error('Error deleting recipe:', xhr);
        }
    });
}

/**
 * Display recipes in the UI
 * @param {Array<SimpleRecipeDto>} recipes - Array of recipe objects
 */
function displayRecipes(recipes) {
    $('#recipeList').empty();
    
    if (recipes.length === 0) {
        $('#recipeList').html('<div class="col-12"><div class="alert alert-info">No recipes available. Add your first recipe!</div></div>');
        return;
    }
    
    // Get the template
    const template = document.getElementById('recipe-card-template');
    
    // Create and append recipe cards
    recipes.forEach(recipe => {
        const recipeCard = document.importNode(template.content, true);
        
        // Set recipe data
        recipeCard.querySelector('.recipe-name').textContent = recipe.name;
        recipeCard.querySelector('.meal-type').textContent = formatMealType(recipe.type);
        recipeCard.querySelector('.meal-type').classList.add(recipe.type);
        recipeCard.querySelector('.cooking-time').textContent = `${recipe.cooking_time_in_minutes} mins`;
        
        // Set data attributes for actions
        const viewButton = recipeCard.querySelector('.view-recipe');
        viewButton.href = `/recipe-detail.html?id=${recipe.id}`;
        viewButton.dataset.id = recipe.id;
        
        const deleteButton = recipeCard.querySelector('.delete-recipe');
        deleteButton.dataset.id = recipe.id;
        
        $('#recipeList').append(recipeCard);
    });
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
    const alertHtml = `<div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>`;
    
    $('#recipeList').prepend(alertHtml);
}

/**
 * Show success message
 * @param {string} message - The success message
 */
function showSuccess(message) {
    const alertHtml = `<div class="alert alert-success alert-dismissible fade show" role="alert">
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>`;
    
    $('#recipeList').prepend(alertHtml);
}