package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Javid
 * IT: IntegrationTest
 * Integration Test for {@link RecipeServiceImpl}
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecipeServiceIT {

    private static final String NEW_DESCRIPTION = "New description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Transactional
    @Test
    void testSaveOfDescription() {
        // given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe recipe = recipes.iterator().next();
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

        // when
        assertNotNull(recipeCommand);
        recipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        // then
        assertNotNull(savedRecipeCommand);
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(recipe.getId(), savedRecipeCommand.getId());
        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
        assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategories().size());
    }

    @Transactional
    @Test
    void testSaveNewCommand() {
        // given
        var recipeCommand = new RecipeCommand();
        recipeCommand.setDescription(NEW_DESCRIPTION);

        // when
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        // then
        assertNotNull(savedRecipeCommand);
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
    }

    @Test
    void testSaveNull() {
        assertNull(recipeService.saveRecipeCommand(null));
    }

}
