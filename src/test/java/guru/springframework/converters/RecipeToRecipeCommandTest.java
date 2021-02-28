package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

    private static final Long RECIPE_ID = 1L;
    private static final Integer COOK_TIME = Integer.valueOf("5");
    private static final Integer PREP_TIME = Integer.valueOf("7");
    private static final String DESCRIPTION = "My Recipe";
    private static final String DIRECTIONS = "Directions";
    private static final Difficulty DIFFICULTY = Difficulty.EASY;
    private static final Integer SERVINGS = Integer.valueOf("3");
    private static final String SOURCE = "Source";
    private static final String URL = "Some URL";
    private static final Long CATEGORY_ID_1 = 1L;
    private static final Long CATEGORY_ID_2 = 2L;
    private static final Long INGREDIENT_ID_1 = 3L;
    private static final Long INGREDIENT_ID_2 = 4L;
    private static final Long NOTES_ID = 9L;
    private static final Byte[] IMAGE;

    static {
        byte[] bytes = "Fake Image String".getBytes();
        IMAGE = new Byte[bytes.length];
        int i = 0;
        for (byte b : bytes) {
            IMAGE[i++] = b;
        }
    }

    RecipeToRecipeCommand converter;

    @BeforeEach
    void setup() {
        converter = new RecipeToRecipeCommand(new CategoryToCategoryCommand(),
                new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
                new NotesToNotesCommand());
    }

    @Test
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParameter() {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    void testWhenNullIngredients() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setIngredients(null);
        // when
        RecipeCommand command = converter.convert(recipe);
        // then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertNotNull(command.getIngredients());
    }

    @Test
    void testWhenEmptyIngredients() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setIngredients(new HashSet<>());
        // when
        RecipeCommand command = converter.convert(recipe);
        // then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertNotNull(command.getIngredients());
    }

    @Test
    void testWhenNullCategory() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setCategories(null);
        // when
        RecipeCommand command = converter.convert(recipe);
        // then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertNotNull(command.getCategories());
    }

    @Test
    void testWhenEmptyCategory() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setIngredients(new HashSet<>());
        // when
        RecipeCommand command = converter.convert(recipe);
        // then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getId());
        assertNotNull(command.getCategories());
    }

    @Test
    void convert() {
        // given
        Notes notes = new Notes();
        notes.setId(NOTES_ID);

        Category category1 = new Category();
        category1.setId(CATEGORY_ID_1);
        Category category2 = new Category();
        category2.setId(CATEGORY_ID_2);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(INGREDIENT_ID_1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGREDIENT_ID_2);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDescription(DESCRIPTION);
        recipe.setDirections(DIRECTIONS);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);
        recipe.setImage(IMAGE);
        recipe.setNotes(notes);
        recipe.getCategories().add(category1);
        recipe.getCategories().add(category2);
        recipe.getIngredients().add(ingredient1);
        recipe.getIngredients().add(ingredient2);

        // when
        RecipeCommand recipeCommand = converter.convert(recipe);

        // then
        assertNotNull(recipeCommand);
        assertEquals(RECIPE_ID, recipeCommand.getId());
        assertEquals(COOK_TIME, recipeCommand.getCookTime());
        assertEquals(PREP_TIME, recipeCommand.getPrepTime());
        assertEquals(DESCRIPTION, recipeCommand.getDescription());
        assertEquals(DIRECTIONS, recipeCommand.getDirections());
        assertEquals(DIFFICULTY, recipeCommand.getDifficulty());
        assertEquals(SERVINGS, recipeCommand.getServings());
        assertEquals(SOURCE, recipeCommand.getSource());
        assertEquals(URL, recipeCommand.getUrl());
        assertArrayEquals(IMAGE, recipeCommand.getImage());
        assertEquals(NOTES_ID, recipeCommand.getNotes().getId());
        assertEquals(2, recipeCommand.getCategories().size());
        assertEquals(2, recipeCommand.getIngredients().size());
    }

}
