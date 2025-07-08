#!/usr/bin/env python3
"""
Generate a PDF with basic recipes for testing file ingestion.
"""
import os
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib.units import inch

def create_recipe_pdf():
    """Create a PDF with 25 basic recipes."""
    
    recipes = [
        {
            "title": "Scrambled Eggs",
            "ingredients": ["3 eggs", "2 tbsp milk", "1 tbsp butter", "Salt and pepper"],
            "instructions": "Beat eggs with milk. Heat butter in pan. Pour in eggs and stir gently until set."
        },
        {
            "title": "Grilled Cheese Sandwich",
            "ingredients": ["2 slices bread", "2 slices cheese", "1 tbsp butter"],
            "instructions": "Butter bread. Place cheese between slices. Grill in pan until golden brown."
        },
        {
            "title": "Pancakes",
            "ingredients": ["1 cup flour", "1 cup milk", "1 egg", "2 tbsp sugar", "1 tsp baking powder"],
            "instructions": "Mix dry ingredients. Combine wet ingredients. Combine both and cook on griddle."
        },
        {
            "title": "Caesar Salad",
            "ingredients": ["1 head romaine lettuce", "1/4 cup parmesan cheese", "Croutons", "Caesar dressing"],
            "instructions": "Chop lettuce. Toss with dressing. Add cheese and croutons."
        },
        {
            "title": "Spaghetti Carbonara",
            "ingredients": ["400g spaghetti", "200g bacon", "3 eggs", "100g parmesan", "Black pepper"],
            "instructions": "Cook pasta. Fry bacon. Mix eggs and cheese. Combine all with hot pasta."
        },
        {
            "title": "Chicken Stir Fry",
            "ingredients": ["500g chicken breast", "Mixed vegetables", "2 tbsp soy sauce", "1 tbsp oil"],
            "instructions": "Cut chicken into strips. Heat oil. Cook chicken, add vegetables and soy sauce."
        },
        {
            "title": "Beef Tacos",
            "ingredients": ["500g ground beef", "Taco shells", "Lettuce", "Tomatoes", "Cheese"],
            "instructions": "Brown beef. Warm taco shells. Fill with beef and toppings."
        },
        {
            "title": "Vegetable Soup",
            "ingredients": ["Mixed vegetables", "4 cups vegetable broth", "1 onion", "2 cloves garlic"],
            "instructions": "Sauté onion and garlic. Add vegetables and broth. Simmer 20 minutes."
        },
        {
            "title": "Chocolate Chip Cookies",
            "ingredients": ["2 cups flour", "1 cup butter", "1 cup sugar", "1 egg", "1 cup chocolate chips"],
            "instructions": "Cream butter and sugar. Add egg. Mix in flour and chips. Bake at 375°F."
        },
        {
            "title": "Banana Bread",
            "ingredients": ["3 ripe bananas", "1/3 cup melted butter", "3/4 cup sugar", "1 egg", "1 tsp vanilla", "1 tsp baking soda", "1.5 cups flour"],
            "instructions": "Mash bananas. Mix with butter. Add sugar, egg, vanilla. Combine dry ingredients and fold in."
        },
        {
            "title": "Tomato Basil Pasta",
            "ingredients": ["400g pasta", "4 tomatoes", "Fresh basil", "3 cloves garlic", "Olive oil"],
            "instructions": "Cook pasta. Dice tomatoes. Sauté garlic. Combine with basil and pasta."
        },
        {
            "title": "Chicken Noodle Soup",
            "ingredients": ["2 chicken breasts", "Egg noodles", "Carrots", "Celery", "Onion", "Chicken broth"],
            "instructions": "Cook chicken and shred. Sauté vegetables. Add broth and noodles. Simmer."
        },
        {
            "title": "Fish and Chips",
            "ingredients": ["4 fish fillets", "Potatoes", "Flour", "Beer", "Oil for frying"],
            "instructions": "Make batter with flour and beer. Coat fish. Fry fish and chips separately."
        },
        {
            "title": "Mushroom Risotto",
            "ingredients": ["1 cup arborio rice", "Mixed mushrooms", "4 cups warm broth", "1 onion", "White wine"],
            "instructions": "Sauté onion and mushrooms. Add rice and wine. Slowly add broth, stirring constantly."
        },
        {
            "title": "Greek Salad",
            "ingredients": ["Cucumbers", "Tomatoes", "Red onion", "Feta cheese", "Olives", "Olive oil"],
            "instructions": "Chop vegetables. Combine with feta and olives. Dress with olive oil."
        },
        {
            "title": "Beef Stew",
            "ingredients": ["2 lbs beef chunks", "Potatoes", "Carrots", "Onions", "Beef broth", "Flour"],
            "instructions": "Brown beef. Add vegetables and broth. Simmer 2 hours until tender."
        },
        {
            "title": "Chicken Curry",
            "ingredients": ["4 chicken thighs", "Coconut milk", "Curry powder", "Onion", "Garlic", "Ginger"],
            "instructions": "Brown chicken. Sauté aromatics. Add curry powder and coconut milk. Simmer."
        },
        {
            "title": "Meatball Subs",
            "ingredients": ["Ground beef", "Breadcrumbs", "Egg", "Sub rolls", "Marinara sauce", "Mozzarella"],
            "instructions": "Mix meat with breadcrumbs and egg. Form balls and cook. Serve in rolls with sauce."
        },
        {
            "title": "Quinoa Salad",
            "ingredients": ["1 cup quinoa", "Vegetables", "Lemon juice", "Olive oil", "Herbs"],
            "instructions": "Cook quinoa. Cool. Mix with chopped vegetables and dressing."
        },
        {
            "title": "Stuffed Peppers",
            "ingredients": ["Bell peppers", "Ground meat", "Rice", "Onion", "Tomato sauce"],
            "instructions": "Hollow peppers. Mix meat with rice and onion. Stuff peppers and bake."
        },
        {
            "title": "Chili Con Carne",
            "ingredients": ["Ground beef", "Kidney beans", "Tomatoes", "Onion", "Chili powder"],
            "instructions": "Brown beef and onion. Add tomatoes, beans, and spices. Simmer 1 hour."
        },
        {
            "title": "Lemon Garlic Chicken",
            "ingredients": ["4 chicken breasts", "Lemon", "Garlic", "Olive oil", "Herbs"],
            "instructions": "Marinate chicken in lemon and garlic. Grill or bake until cooked through."
        },
        {
            "title": "Vegetable Fried Rice",
            "ingredients": ["Cooked rice", "Mixed vegetables", "Eggs", "Soy sauce", "Sesame oil"],
            "instructions": "Heat oil. Scramble eggs. Add rice and vegetables. Season with soy sauce."
        },
        {
            "title": "Caprese Salad",
            "ingredients": ["Tomatoes", "Fresh mozzarella", "Fresh basil", "Olive oil", "Balsamic vinegar"],
            "instructions": "Slice tomatoes and mozzarella. Arrange with basil. Drizzle with oil and vinegar."
        },
        {
            "title": "Chicken Fajitas",
            "ingredients": ["Chicken strips", "Bell peppers", "Onions", "Tortillas", "Fajita seasoning"],
            "instructions": "Cook chicken with seasoning. Sauté peppers and onions. Serve in tortillas."
        }
    ]
    
    # Create PDF
    current_dir = os.path.dirname(os.path.abspath(__file__))
    parent_dir = os.path.dirname(current_dir)
    pdf_path = os.path.join(parent_dir, "basic_recipes.pdf")
    doc = SimpleDocTemplate(pdf_path, pagesize=letter)
    styles = getSampleStyleSheet()
    story = []
    
    # Title page
    title = Paragraph("Basic Recipes Collection", styles['Title'])
    story.append(title)
    story.append(Spacer(1, 0.5*inch))
    
    # Add recipes
    for i, recipe in enumerate(recipes, 1):
        # Recipe title
        recipe_title = Paragraph(f"{i}. {recipe['title']}", styles['Heading2'])
        story.append(recipe_title)
        story.append(Spacer(1, 0.1*inch))
        
        # Ingredients
        ingredients_title = Paragraph("Ingredients:", styles['Heading3'])
        story.append(ingredients_title)
        
        for ingredient in recipe['ingredients']:
            ingredient_text = Paragraph(f"• {ingredient}", styles['Normal'])
            story.append(ingredient_text)
        
        story.append(Spacer(1, 0.1*inch))
        
        # Instructions
        instructions_title = Paragraph("Instructions:", styles['Heading3'])
        story.append(instructions_title)
        
        instructions_text = Paragraph(recipe['instructions'], styles['Normal'])
        story.append(instructions_text)
        
        story.append(Spacer(1, 0.3*inch))
    
    # Build PDF
    doc.build(story)
    print("PDF created successfully")

if __name__ == "__main__":
    create_recipe_pdf()