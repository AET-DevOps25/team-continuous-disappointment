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
            "title": "French Toast",
            "ingredients": ["4 slices bread", "2 eggs", "1/2 cup milk", "1 tsp vanilla", "Butter", "Maple syrup"],
            "instructions": "Beat eggs with milk and vanilla. Dip bread slices. Cook in buttered pan until golden."
        },
        {
            "title": "Beef Burgers",
            "ingredients": ["1 lb ground beef", "Burger buns", "Lettuce", "Tomatoes", "Onions", "Cheese"],
            "instructions": "Form beef into patties. Grill or pan-fry. Assemble with toppings on buns."
        },
        {
            "title": "Chicken Caesar Wrap",
            "ingredients": ["Grilled chicken", "Tortilla", "Romaine lettuce", "Caesar dressing", "Parmesan"],
            "instructions": "Slice chicken. Toss lettuce with dressing. Wrap in tortilla with cheese."
        },
        {
            "title": "Mushroom Omelette",
            "ingredients": ["3 eggs", "Mushrooms", "Cheese", "Butter", "Salt", "Pepper"],
            "instructions": "Sauté mushrooms. Beat eggs. Cook in buttered pan, add mushrooms and cheese. Fold."
        },
        {
            "title": "Tuna Salad",
            "ingredients": ["2 cans tuna", "Mayo", "Celery", "Red onion", "Lemon juice", "Bread"],
            "instructions": "Drain tuna. Mix with mayo, diced celery, and onion. Add lemon juice. Serve on bread."
        },
        {
            "title": "Pork Chops",
            "ingredients": ["4 pork chops", "Salt", "Pepper", "Garlic powder", "Olive oil"],
            "instructions": "Season chops. Heat oil in pan. Cook chops 4-5 minutes per side until done."
        },
        {
            "title": "Shrimp Scampi",
            "ingredients": ["1 lb shrimp", "Pasta", "Garlic", "White wine", "Butter", "Parsley"],
            "instructions": "Cook pasta. Sauté garlic. Add shrimp and wine. Toss with butter and parsley."
        },
        {
            "title": "Beef Bolognese",
            "ingredients": ["Ground beef", "Onions", "Carrots", "Celery", "Tomatoes", "Red wine", "Pasta"],
            "instructions": "Brown beef. Add diced vegetables. Pour in wine and tomatoes. Simmer 2 hours."
        },
        {
            "title": "Chicken Teriyaki",
            "ingredients": ["Chicken thighs", "Soy sauce", "Mirin", "Sugar", "Ginger", "Garlic"],
            "instructions": "Marinate chicken in teriyaki sauce. Grill or bake until cooked through."
        },
        {
            "title": "Spinach Salad",
            "ingredients": ["Fresh spinach", "Bacon", "Hard-boiled eggs", "Mushrooms", "Balsamic vinaigrette"],
            "instructions": "Cook bacon and chop. Slice eggs and mushrooms. Toss spinach with dressing and toppings."
        },
        {
            "title": "Lamb Chops",
            "ingredients": ["8 lamb chops", "Rosemary", "Garlic", "Olive oil", "Salt", "Pepper"],
            "instructions": "Season chops with herbs. Heat oil. Cook chops 3-4 minutes per side for medium."
        },
        {
            "title": "Clam Chowder",
            "ingredients": ["Clams", "Potatoes", "Onions", "Bacon", "Heavy cream", "Thyme"],
            "instructions": "Cook bacon. Sauté onions. Add potatoes and clam juice. Simmer, then add cream."
        },
        {
            "title": "Turkey Sandwich",
            "ingredients": ["Sliced turkey", "Bread", "Lettuce", "Tomatoes", "Mayo", "Cheese"],
            "instructions": "Layer turkey on bread with vegetables and condiments. Slice and serve."
        },
        {
            "title": "Pork Tenderloin",
            "ingredients": ["Pork tenderloin", "Herbs", "Garlic", "Olive oil", "Salt", "Pepper"],
            "instructions": "Season tenderloin. Sear in hot pan. Finish in oven at 400°F for 15 minutes."
        },
        {
            "title": "Chicken Parmesan",
            "ingredients": ["Chicken breasts", "Breadcrumbs", "Parmesan", "Eggs", "Marinara", "Mozzarella"],
            "instructions": "Bread chicken. Fry until golden. Top with sauce and cheese. Bake until melted."
        },
        {
            "title": "Ratatouille",
            "ingredients": ["Eggplant", "Zucchini", "Tomatoes", "Bell peppers", "Onions", "Herbs"],
            "instructions": "Dice vegetables. Sauté onions. Add vegetables and herbs. Simmer until tender."
        },
        {
            "title": "Salmon Fillet",
            "ingredients": ["Salmon fillets", "Lemon", "Dill", "Olive oil", "Salt", "Pepper"],
            "instructions": "Season salmon. Heat oil in pan. Cook skin-side down, then flip. Finish with lemon."
        },
        {
            "title": "Chicken Wings",
            "ingredients": ["Chicken wings", "Hot sauce", "Butter", "Garlic powder", "Celery salt"],
            "instructions": "Bake wings at 425°F. Mix sauce with butter. Toss wings in sauce."
        },
        {
            "title": "Beef Stroganoff",
            "ingredients": ["Beef strips", "Mushrooms", "Onions", "Sour cream", "Beef broth", "Egg noodles"],
            "instructions": "Brown beef. Sauté mushrooms and onions. Add broth and sour cream. Serve over noodles."
        },
        {
            "title": "Cobb Salad",
            "ingredients": ["Mixed greens", "Chicken", "Bacon", "Blue cheese", "Eggs", "Avocado", "Tomatoes"],
            "instructions": "Arrange ingredients in rows over greens. Serve with blue cheese dressing."
        },
        {
            "title": "Pork Ribs",
            "ingredients": ["Pork ribs", "BBQ sauce", "Brown sugar", "Paprika", "Garlic powder"],
            "instructions": "Season ribs. Cook low and slow. Brush with sauce in last 30 minutes."
        },
        {
            "title": "Chicken Pot Pie",
            "ingredients": ["Chicken", "Mixed vegetables", "Chicken broth", "Flour", "Pie crust"],
            "instructions": "Cook chicken and vegetables. Make gravy. Fill crust and top. Bake until golden."
        },
        {
            "title": "Duck Breast",
            "ingredients": ["Duck breasts", "Orange", "Honey", "Soy sauce", "Ginger"],
            "instructions": "Score duck skin. Sear skin-side down. Flip and finish in oven. Glaze with sauce."
        },
        {
            "title": "Lobster Bisque",
            "ingredients": ["Lobster shells", "Sherry", "Heavy cream", "Tomato paste", "Onions", "Herbs"],
            "instructions": "Sauté shells. Add vegetables and sherry. Simmer, strain, and finish with cream."
        },
        {
            "title": "Beef Wellington",
            "ingredients": ["Beef tenderloin", "Puff pastry", "Mushrooms", "Prosciutto", "Egg wash"],
            "instructions": "Sear beef. Wrap in mushroom mixture and prosciutto. Encase in pastry. Bake until golden."
        }
    ]
    
    # Create PDF
    current_dir = os.path.dirname(os.path.abspath(__file__))
    parent_dir = os.path.dirname(current_dir)
    pdf_path = os.path.join(parent_dir, "basic_recipes2.pdf")
    doc = SimpleDocTemplate(pdf_path, pagesize=letter)
    styles = getSampleStyleSheet()
    story = []
    
    # Title page
    title = Paragraph("Basic Recipes Collection 2", styles['Title'])
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