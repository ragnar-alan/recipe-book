/**
 * JavaScript file for the Recipe Form page
 * Handles creating new recipes
 */

// Define DTO-matching objects
/**
 * CreateRecipeRequest structure:
 * {
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
    // Set up form submission
    $('#recipeForm').on('submit', function(e) {
        e.preventDefault();
        
        if (validateForm()) {
            submitRecipe();
        }
    });
    
    // Set up add ingredient button
    $('#addIngredient').click(function() {
        addIngredientField();
    });
    
    // Set up remove ingredient button (using event delegation)
    $('#ingredientsList').on('click', '.remove-ingredient', function() {
        // Don't remove if it's the only ingredient
        if ($('.ingredient-item').length > 1) {
            $(this).closest('.ingredient-item').remove();
        } else {
            // Clear the fields instead of removing
            $(this).closest('.ingredient-item').find('input').val('');
            showIngredientError('At least one ingredient is required');
        }
    });
});

/**
 * Add a new ingredient field to the form
 */
function addIngredientField() {
    // Get the template
    const template = document.getElementById('ingredient-template');
    const ingredientItem = document.importNode(template.content, true);
    
    // Append to the ingredients list
    $('#ingredientsList').append(ingredientItem);
}

/**
 * Validate the form before submission
 * @returns {boolean} True if the form is valid
 */
function validateForm() {
    let isValid = true;
    $('#formErrors').addClass('d-none');
    
    // Reset all validation states
    $('.is-invalid').removeClass('is-invalid');
    
    // Validate required fields
    if (!$('#name').val().trim()) {
        $('#name').addClass('is-invalid');
        isValid = false;
    }
    
    if (!$('#description').val().trim()) {
        $('#description').addClass('is-invalid');
        isValid = false;
    }
    
    if (!$('#cooking_time_in_minutes').val() || parseInt($('#cooking_time_in_minutes').val()) < 1) {
        $('#cooking_time_in_minutes').addClass('is-invalid');
        isValid = false;
    }
    
    if (!$('#type').val()) {
        $('#type').addClass('is-invalid');
        isValid = false;
    }
    
    // Validate ingredients
    let hasValidIngredient = false;
    $('.ingredient-item').each(function() {
        const nameInput = $(this).find('.ingredient-name');
        const amountInput = $(this).find('.ingredient-amount');
        
        const name = nameInput.val().trim();
        const amount = amountInput.val();
        
        if (name && amount && parseFloat(amount) > 0) {
            hasValidIngredient = true;
        } else if (name || amount) {
            // If one field is filled but not both
            if (!name) nameInput.addClass('is-invalid');
            if (!amount || parseFloat(amount) <= 0) amountInput.addClass('is-invalid');
            isValid = false;
        }
    });
    
    if (!hasValidIngredient) {
        showIngredientError('At least one ingredient with name and amount is required');
        isValid = false;
    }
    
    if (!isValid) {
        $('#formErrors').removeClass('d-none');
    }
    
    return isValid;
}

/**
 * Show error for ingredients section
 * @param {string} message - Error message
 */
function showIngredientError(message) {
    $('#ingredientsError').text(message).parent().addClass('was-validated');
}

/**
 * Submit the recipe to the API
 */
function submitRecipe() {
    // Collect form data
    const recipe = {
        name: $('#name').val().trim(),
        description: $('#description').val().trim(),
        cooking_time_in_minutes: parseInt($('#cooking_time_in_minutes').val()),
        type: $('#type').val(),
        ingredients: []
    };
    
    // Collect ingredients
    $('.ingredient-item').each(function() {
        const name = $(this).find('.ingredient-name').val().trim();
        const amount = $(this).find('.ingredient-amount').val();
        const unit = $(this).find('.ingredient-unit').val().trim();
        
        if (name && amount) {
            recipe.ingredients.push({
                name: name,
                amount: parseFloat(amount),
                unit: unit || null
            });
        }
    });
    
    // Disable form during submission
    const submitButton = $('#recipeForm button[type="submit"]');
    const originalButtonText = submitButton.text();
    submitButton.prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Saving...');
    
    // Submit to API
    $.ajax({
        url: '/api/v1/recipes',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(recipe),
        success: function() {
            // Redirect to home page on success
            window.location.href = '/?success=created';
        },
        error: function(xhr) {
            // Re-enable form
            submitButton.prop('disabled', false).text(originalButtonText);
            
            // Show error
            let errorMessage = 'Failed to create recipe. Please try again.';
            
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }
            
            $('#formErrors').text(errorMessage).removeClass('d-none');
            console.error('Error creating recipe:', xhr);
        }
    });
}