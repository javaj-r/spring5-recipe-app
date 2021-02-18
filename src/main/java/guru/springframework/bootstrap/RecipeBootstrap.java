package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap Data");

    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        val uomEach = "Each";
        val uomTeaspoon = "Teaspoon";
        val uomTablespoon = "Tablespoon";
        val uomDash = "Dash";
        val uomPint = "Pint";
        val uomCup = "Cup";
        val categoryAmerican = "American";
        val categoryMexican = "Mexican";

        // get UOMs
        val uomMap = getUnitOfMeasures(uomEach, uomTeaspoon, uomTablespoon, uomDash, uomPint, uomCup);

        // get Categories
        val categoryMap = getCategories(categoryAmerican, categoryMexican);

        Recipe.Builder recipeBuilder;

        // Yummy Guacamole
        var directions = "1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. "
                + "Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. "
                + "(See How to Cut and Peel an Avocado.) Place in a bowl."
                + "\n\n" + "2 Mash with a fork: Using a fork, roughly mash the avocado. "
                + "(Don't overdo it! The guacamole should be a little chunky.)"
                + "\n\n" + "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. "
                + "The acid in the lime juice will provide some balance to the richness of the avocado "
                + "and will help delay the avocados from turning brown."
                + "\n" + "Add the chopped onion, cilantro, black pepper, and chiles. "
                + "Chili peppers vary individually in their hotness. "
                + "So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness."
                + "\n" + "Remember that much of this is done to taste because of the variability in the fresh ingredients. "
                + "Start with this recipe and adjust to your taste."
                + "\n" + "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, "
                + "add it just before serving."
                + "\n\n" + "4 Serve: Serve immediately, or if making a few hours ahead, "
                + "place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. "
                + "(The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.";

        var recipeNotes = "Simple Guacamole: The simplest version of guacamole is just mashed avocados with salt. "
                + "Don’t let the lack of availability of other ingredients stop you from making guacamole."
                + "\n" + "Quick guacamole: For a very quick guacamole just "
                + "take a 1/4 cup of salsa and mix it in with your mashed avocados."
                + "\n" + "Don’t have enough avocados? To extend a limited supply of avocados, "
                + "add either sour cream or cottage cheese to your guacamole dip. "
                + "Purists may be horrified, but so what? It tastes great.";

        recipeBuilder = Recipe.builder()
                .description("Perfect Guacamole")
                .prepTime(10)
                .cookTime(0)
                .servings(4)
                .source("Elise Bauer")
                .url("https://www.simplyrecipes.com/recipes/perfect_guacamole/")
                .directions(directions)
                .difficulty(Difficulty.EASY)
                .notes(recipeNotes)
                .addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("Kosher salt", new BigDecimal(".25"), uomMap.get(uomTeaspoon)))
                .addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(1), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("minced red onion or thinly sliced green anion", new BigDecimal(2), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("Cilantro", new BigDecimal(2), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal(1), uomMap.get(uomDash)))
                .addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(".5"), uomMap.get(uomEach)))
                .addCategory(categoryMap.get(categoryAmerican))
                .addCategory(categoryMap.get(categoryMexican));

        val guacRecipe = recipeBuilder.build();

        recipes.add(guacRecipe);

        // Yummy Tacos
        directions = "1 Prepare a gas or charcoal grill for medium-high, direct heat."
                + "\n\n" + "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, "
                + "oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make"
                + " a loose paste. Add the chicken to the bowl and toss to coat all over."
                + "\n" + "Set aside to marinate while the grill heats and you prepare the rest of the toppings."
                + "\n\n" + "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer "
                + "inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes."
                + "\n\n" + "4 Warm the tortillas: Place each tortilla on the grill or on a hot, "
                + "dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, "
                + "turn it with tongs and heat for a few seconds on the other side."
                + "\n" + "Wrap warmed tortillas in a tea towel to keep them warm until serving."
                + "\n\n" + "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. "
                + "Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. "
                + "Drizzle with the thinned sour cream. Serve with lime wedges.";

        recipeNotes = "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!"
                + "\n" + "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, "
                + "and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings."
                + "\n" + "Grill the chicken, then let it rest while you warm the tortillas. "
                + "Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!"
                + "\n" + "Spicy Grilled Chicken TacosThe ancho chiles I use in the marinade are named for their wide shape. "
                + "They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. "
                + "You can find ancho chile powder at any markets that sell Mexican ingredients, or online."
                + "\n" + "I like to put all the toppings in little bowls on a big platter at the center of the table: "
                + "avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, "
                + "as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and "
                + "I think it adds a nice green crunch to the tacos."
                + "\n" + "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them."
                + "\n" + "You could also easily double or even triple this recipe for a larger party. "
                + "A taco and a cold beer on a warm day? Now that’s living!";

        recipeBuilder = Recipe.builder()
                .description("Spicy Grilled Chicken Tacos")
                .prepTime(20)
                .cookTime(15)
                .servings(6)
                .source("Sally Vargas")
                .url("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/")
                .directions(directions)
                .difficulty(Difficulty.MODERATE)
                .notes(recipeNotes)
                .addIngredient(new Ingredient("Ancho Chili Powder", new BigDecimal(2), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("Dried Oregano", new BigDecimal(1), uomMap.get(uomTeaspoon)))
                .addIngredient(new Ingredient("Dried Cumin", new BigDecimal(1), uomMap.get(uomTeaspoon)))
                .addIngredient(new Ingredient("Sugar", new BigDecimal(1), uomMap.get(uomTeaspoon)))
                .addIngredient(new Ingredient("Salt", new BigDecimal(".5"), uomMap.get(uomTeaspoon)))
                .addIngredient(new Ingredient("Clove of Garlic, Chopped", new BigDecimal(1), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("finely grated orange zest", new BigDecimal(1), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("Olive Oil", new BigDecimal(2), uomMap.get(uomTablespoon)))
                .addIngredient(new Ingredient("boneless chicken thighs", new BigDecimal(6), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("packed baby arugula ", new BigDecimal(3), uomMap.get(uomCup)))
                .addIngredient(new Ingredient("medium ripe avocados, sliced", new BigDecimal(2), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(".5"), uomMap.get(uomPint)))
                .addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(".25"), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("Roughly chopped cilantro", new BigDecimal(1), uomMap.get(uomEach)))
                .addIngredient(new Ingredient("cup sour cream thinned with 1/4 cup milk", new BigDecimal(".5"), uomMap.get(uomCup)))
                .addIngredient(new Ingredient("milk", new BigDecimal(".25"), uomMap.get(uomCup)))
                .addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(1), uomMap.get(uomEach)))
                .addCategory(categoryMap.get(categoryAmerican))
                .addCategory(categoryMap.get(categoryMexican));

        val tacosRecipe = recipeBuilder.build();

        recipes.add(tacosRecipe);

        return recipes;
    }

    private Map<String, UnitOfMeasure> getUnitOfMeasures(String... keys) {
        final Map<String, UnitOfMeasure> uomMap = new HashMap<>();
        for (String key : keys) {
            unitOfMeasureRepository.findByDescription(key).ifPresentOrElse(unitOfMeasure -> uomMap.put(key, unitOfMeasure),
                    () -> throwRuntimeException("Expected UOM Not Found: " + key));
        }
        return uomMap;
    }

    private Map<String, Category> getCategories(String... keys) {
        final Map<String, Category> uomMap = new HashMap<>();
        for (String key : keys) {
            categoryRepository.findByDescription(key).ifPresentOrElse(unitOfMeasure -> uomMap.put(key, unitOfMeasure),
                    () -> throwRuntimeException("Expected Category Not Found: " + key));
        }
        return uomMap;
    }

    private void throwRuntimeException(String message) {
        throw new BootstrapRuntimeException(message);
    }

    static class BootstrapRuntimeException extends RuntimeException {
        public BootstrapRuntimeException(String message) {
            super(message);
        }
    }
}
