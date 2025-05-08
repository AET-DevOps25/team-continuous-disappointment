# RecipAI

## Main Functionality

The primary functionality of the application is to enable users to generate and explore recipes through a natural language interface powered by a LLM. The application allows users to search for recipes based on ingredients, generate meal plans, and receive step-by-step cooking instructions tailored to their preferences and dietary requirements.

## Intended Users

The application is designed for home cooks, culinary enthusiasts, individuals with dietary restrictions, and anyone interested in exploring or generating recipes in an intuitive and flexible manner. It is particularly useful for users who wish to interact with a recipe database using natural language queries.

## Integration of Generative AI

Generative AI is integrated meaningfully through a dedicated LLM microservice developed in Python. This service processes user inputs in natural language, generates recipes based on the provided ingredients, modifies existing recipes according to user needs, and provides meal suggestions. The use of GenAI enhances the user experience by offering creative, context-aware, and highly adaptable culinary solutions.

## Functional Scenarios

1. **Ingredient-Based Recipe Generation**: A user inputs, "Suggest a quick dinner recipe with chicken and broccoli." The system uses the LLM to generate a relevant recipe, which is presented to the user through the user interface.

2. **Recipe Modification**: A user submits a traditional recipe and requests, "Make this vegan." The LLM identifies non-vegan ingredients and substitutes them with plant-based alternatives, returning a modified version of the recipe.

3. **Meal Planning**: A user asks for a weekly meal plan. The LLM generates a diverse and nutritionally balanced plan, optionally based on dietary restrictions or cuisine preferences.

4. **Ingredient-Limited Cooking**: A user specifies available ingredients, such as "eggs, spinach, and cheese," and the system suggests recipes that can be prepared using those ingredients, optimizing for simplicity and flavor.
