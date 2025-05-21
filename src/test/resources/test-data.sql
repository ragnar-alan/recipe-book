-- Create hibernate_sequence if not exists
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 100000 INCREMENT BY 1;

-- Create recipes table if not exists
CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    ingredients JSONB NOT NULL,
    cooking_time_in_minutes INTEGER NOT NULL,
    type_of_meal VARCHAR(50) NOT NULL,
    created_at TIMESTAMP
);
-- Reset the table for testing
TRUNCATE TABLE recipes CASCADE;

-- Create indexes on name if not exists
CREATE INDEX IF NOT EXISTS idx_recipes_name ON recipes(name);
CREATE INDEX IF NOT EXISTS recipe_search_idx ON recipes USING GIN(to_tsvector('english', name || ' ' || ingredients || ' ' || description));

-- Alter sequence to restart from original value
ALTER SEQUENCE hibernate_sequence RESTART WITH 100000;

-- Set the sequence to the max id in the table
SELECT setval('hibernate_sequence', (SELECT MAX(id) FROM recipes));

-- Insert 20 random recipes
INSERT INTO recipes (id, name, description, ingredients, cooking_time_in_minutes, type_of_meal, created_at)
VALUES
(nextval('hibernate_sequence'), 'Classic Pancakes', 'Fluffy and delicious breakfast pancakes', '[{"name":"Flour","amount":2,"unit":"cups"},{"name":"Milk","amount":2,"unit":"cups"},{"name":"Eggs","amount":2,"unit":""},{"name":"Sugar","amount":2,"unit":"tablespoons"},{"name":"Baking Powder","amount":1,"unit":"tablespoon"}]', 20, 'BREAKFAST', NOW()),
(nextval('hibernate_sequence'), 'Spaghetti Carbonara', 'Traditional Italian pasta dish with eggs and bacon', '[{"name":"Spaghetti","amount":500,"unit":"g"},{"name":"Eggs","amount":4,"unit":""},{"name":"Bacon","amount":200,"unit":"g"},{"name":"Parmesan","amount":100,"unit":"g"},{"name":"Black Pepper","amount":1,"unit":"teaspoon"}]', 30, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Caesar Salad', 'Classic salad with romaine lettuce and croutons', '[{"name":"Romaine Lettuce","amount":1,"unit":"head"},{"name":"Croutons","amount":1,"unit":"cup"},{"name":"Parmesan","amount":50,"unit":"g"},{"name":"Caesar Dressing","amount":4,"unit":"tablespoons"}]', 15, 'LUNCH', NOW()),
(nextval('hibernate_sequence'), 'Chicken Curry', 'Spicy Indian-style curry with tender chicken', '[{"name":"Chicken","amount":500,"unit":"g"},{"name":"Curry Powder","amount":2,"unit":"tablespoons"},{"name":"Coconut Milk","amount":400,"unit":"ml"},{"name":"Onion","amount":1,"unit":""},{"name":"Garlic","amount":3,"unit":"cloves"}]', 45, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Avocado Toast', 'Simple and nutritious breakfast', '[{"name":"Bread","amount":2,"unit":"slices"},{"name":"Avocado","amount":1,"unit":""},{"name":"Lemon Juice","amount":1,"unit":"teaspoon"},{"name":"Salt","amount":0.8,"unit":"pinch"},{"name":"Pepper","amount":1,"unit":"pinch"}]', 10, 'BREAKFAST', NOW()),
(nextval('hibernate_sequence'), 'Beef Stir Fry', 'Quick and tasty Asian-inspired dish', '[{"name":"Beef","amount":400,"unit":"g"},{"name":"Bell Peppers","amount":2,"unit":""},{"name":"Broccoli","amount":1,"unit":"head"},{"name":"Soy Sauce","amount":3,"unit":"tablespoons"},{"name":"Ginger","amount":1,"unit":"tablespoon"}]', 25, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Greek Yogurt Parfait', 'Healthy breakfast with layers of yogurt and fruits', '[{"name":"Greek Yogurt","amount":200,"unit":"g"},{"name":"Berries","amount":100,"unit":"g"},{"name":"Honey","amount":2,"unit":"tablespoons"},{"name":"Granola","amount":50,"unit":"g"}]', 5, 'BREAKFAST', NOW()),
(nextval('hibernate_sequence'), 'Mushroom Risotto', 'Creamy Italian rice dish with mushrooms', '[{"name":"Arborio Rice","amount":300,"unit":"g"},{"name":"Mushrooms","amount":250,"unit":"g"},{"name":"Onion","amount":1,"unit":""},{"name":"White Wine","amount":100,"unit":"ml"},{"name":"Vegetable Stock","amount":1,"unit":"liter"},{"name":"Parmesan","amount":50.5,"unit":"g"}]', 40, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Tuna Sandwich', 'Classic lunch option with tuna and mayo', '[{"name":"Bread","amount":2,"unit":"slices"},{"name":"Tuna","amount":1,"unit":"can"},{"name":"Mayonnaise","amount":2,"unit":"tablespoons"},{"name":"Lettuce","amount":2,"unit":"leaves"},{"name":"Tomato","amount":1,"unit":""}]', 10, 'LUNCH', NOW()),
(nextval('hibernate_sequence'), 'Vegetable Soup', 'Hearty and healthy soup with mixed vegetables', '[{"name":"Carrots","amount":2,"unit":""},{"name":"Celery","amount":2,"unit":"stalks"},{"name":"Onion","amount":1,"unit":""},{"name":"Potatoes","amount":2,"unit":""},{"name":"Vegetable Stock","amount":1,"unit":"liter"}]', 35, 'LUNCH', NOW()),
(nextval('hibernate_sequence'), 'Chocolate Chip Cookies', 'Classic homemade cookies with chocolate chips', '[{"name":"Flour","amount":2,"unit":"cups"},{"name":"Butter","amount":200,"unit":"g"},{"name":"Sugar","amount":0.75,"unit":"cup"},{"name":"Eggs","amount":2,"unit":""},{"name":"Chocolate Chips","amount":200,"unit":"g"}]', 25, 'BREAKFAST', NOW()),
(nextval('hibernate_sequence'), 'Beef Lasagna', 'Traditional Italian layered pasta dish', '[{"name":"Lasagna Sheets","amount":12,"unit":""},{"name":"Ground Beef","amount":500,"unit":"g"},{"name":"Tomato Sauce","amount":500,"unit":"ml"},{"name":"Bechamel Sauce","amount":500,"unit":"ml"},{"name":"Mozzarella","amount":200,"unit":"g"}]', 60, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Quinoa Salad', 'Nutritious salad with quinoa and vegetables', '[{"name":"Quinoa","amount":200,"unit":"g"},{"name":"Cucumber","amount":1,"unit":""},{"name":"Cherry Tomatoes","amount":200,"unit":"g"},{"name":"Feta Cheese","amount":100,"unit":"g"},{"name":"Olive Oil","amount":2,"unit":"tablespoons"}]', 20, 'LUNCH', NOW()),
(nextval('hibernate_sequence'), 'Banana Smoothie', 'Quick and refreshing breakfast drink', '[{"name":"Bananas","amount":2,"unit":""},{"name":"Milk","amount":250,"unit":"ml"},{"name":"Honey","amount":1,"unit":"tablespoon"},{"name":"Ice Cubes","amount":5,"unit":""}]', 5, 'BREAKFAST', NOW()),
(nextval('hibernate_sequence'), 'Grilled Salmon', 'Healthy fish dish with lemon and herbs', '[{"name":"Salmon Fillet","amount":400,"unit":"g"},{"name":"Lemon","amount":1,"unit":""},{"name":"Dill","amount":2,"unit":"tablespoons"},{"name":"Olive Oil","amount":2,"unit":"tablespoons"},{"name":"Salt","amount":1,"unit":"teaspoon"}]', 25, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Caprese Salad', 'Simple Italian salad with tomatoes and mozzarella', '[{"name":"Tomatoes","amount":3,"unit":""},{"name":"Mozzarella","amount":200,"unit":"g"},{"name":"Basil","amount":10,"unit":"leaves"},{"name":"Olive Oil","amount":2,"unit":"tablespoons"},{"name":"Balsamic Vinegar","amount":1,"unit":"tablespoon"}]', 10, 'LUNCH', NOW()),
(nextval('hibernate_sequence'), 'Beef Tacos', 'Mexican dish with seasoned beef and toppings', '[{"name":"Ground Beef","amount":500,"unit":"g"},{"name":"Taco Shells","amount":8,"unit":""},{"name":"Lettuce","amount":100,"unit":"g"},{"name":"Tomatoes","amount":2,"unit":""},{"name":"Cheese","amount":100,"unit":"g"},{"name":"Taco Seasoning","amount":2.5,"unit":"tablespoons"}]', 30, 'DINNER', NOW()),
(nextval('hibernate_sequence'), 'Oatmeal with Fruits', 'Healthy breakfast with oats and fresh fruits', '[{"name":"Oats","amount":100,"unit":"g"},{"name":"Milk","amount":250,"unit":"ml"},{"name":"Banana","amount":1,"unit":""},{"name":"Berries","amount":50,"unit":"g"},{"name":"Honey","amount":1,"unit":"tablespoon"}]', 15, 'BREAKFAST', NOW()),
(nextval('hibernate_sequence'), 'Vegetable Stir Fry', 'Quick and healthy vegetable dish', '[{"name":"Mixed Vegetables","amount":500,"unit":"g"},{"name":"Tofu","amount":200,"unit":"g"},{"name":"Soy Sauce","amount":3,"unit":"tablespoons"},{"name":"Garlic","amount":2,"unit":"cloves"},{"name":"Ginger","amount":1.6,"unit":"tablespoon"}]', 20, 'LUNCH', NOW()),
(nextval('hibernate_sequence'), 'Chocolate Cake', 'Rich and moist chocolate dessert', '[{"name":"Flour","amount":2,"unit":"cups"},{"name":"Sugar","amount":1.5,"unit":"cups"},{"name":"Cocoa Powder","amount": 0.75,"unit":"cup"},{"name":"Eggs","amount":3,"unit":""},{"name":"Butter","amount":200,"unit":"g"},{"name":"Milk","amount":1.2,"unit":"cup"}]', 50, 'DINNER', NOW());

