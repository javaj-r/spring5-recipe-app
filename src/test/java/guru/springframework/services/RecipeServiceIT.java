package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    void findCommandById() {
        // given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Long id = recipes.iterator().next().getId();

        // when
        RecipeCommand recipeCommand = recipeService.findCommandById(id);

        // then
        assertNotNull(recipeCommand);
        assertEquals(id, recipeCommand.getId());
    }

    @Test
    void findCommandByIdNotFound() {
        // given
        Set<Long> idSet = new HashSet<>();
        recipeRepository.findAll().forEach(recipe -> idSet.add(recipe.getId()));
        Long id = notExistingId(idSet, 1L);

        // when
        Executable executable = () -> recipeService.findCommandById(id);

        // then
        assertThrows(RuntimeException.class, executable);
    }

    private Long notExistingId(final Set<Long> idsSet, final Long id) {
        return idsSet.contains(id) ? notExistingId(idsSet, id + 1) : id;
    }

}
