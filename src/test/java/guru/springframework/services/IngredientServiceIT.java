package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IngredientServiceIT {

    private static final String NEW_DESCRIPTION = "New description";

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UnitOfMeasureRepository unitRepository;

    @Autowired
    UnitOfMeasureToUnitOfMeasureCommand unitToCommand;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    IngredientToIngredientCommand ingredientToCommand;

    @Transactional
    @Test
    void saveIngredientCommandUpdate() {
        // given
        Recipe recipe = recipeRepository.findAll().iterator().next();
        Set<Ingredient> ingredients = recipe.getIngredients();
        IngredientCommand command = ingredientToCommand.convert(ingredients.iterator().next());

        // when
        assertNotNull(command);
        command.setDescription(NEW_DESCRIPTION);
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        // then
        assertNotNull(savedCommand);
        assertEquals(NEW_DESCRIPTION, savedCommand.getDescription());
        assertEquals(command.getId(), savedCommand.getId());
        assertEquals(command.getRecipeId(), savedCommand.getRecipeId());
        assertEquals(command.getAmount(), savedCommand.getAmount());
        assertEquals(command.getUnitOfMeasure().getId(), savedCommand.getUnitOfMeasure().getId());
    }

    @Transactional
    @Test
    void saveNewIngredientCommand() {
        // given
        UnitOfMeasure unit = unitRepository.findAll().iterator().next();

        Recipe recipe = recipeRepository.findAll().iterator().next();

        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(recipe.getId());
        command.setDescription(NEW_DESCRIPTION);
        command.setUnitOfMeasure(unitToCommand.convert(unit));

        // when
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        // then
        assertNotNull(savedCommand);
        assertNotNull(savedCommand.getId());
        assertEquals(NEW_DESCRIPTION, savedCommand.getDescription());
        assertEquals(recipe.getId(), savedCommand.getRecipeId());
        assertEquals(command.getAmount(), savedCommand.getAmount());
        assertEquals(command.getUnitOfMeasure().getId(), savedCommand.getUnitOfMeasure().getId());
    }

    @Test
    void saveNull() {
        assertNull(ingredientService.saveIngredientCommand(null));
    }

    @Transactional
    @Test
    void deleteByRecipeIdAndIngredientId() {
        // given
        Recipe recipe = recipeRepository.findAll().iterator().next();
        Ingredient ingredient = recipe.getIngredients().iterator().next();

        // when
        ingredientService.deleteByRecipeIdAndIngredientId(recipe.getId(), ingredient.getId());
        Recipe savedRecipe = recipeRepository.findById(recipe.getId()).orElseThrow();
        Set<Long> ids = savedRecipe.getIngredients().stream().map(Ingredient::getId).collect(Collectors.toSet());

        // then
        assertFalse(ids.contains(ingredient.getId()));
    }
}