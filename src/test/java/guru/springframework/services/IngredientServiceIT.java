package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IngredientServiceIT {

    private static final String NEW_DESCRIPTION = "New description";

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    IngredientToIngredientCommand ingredientToCommand;

    @Transactional
    @Test
    void saveIngredientCommand() {
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

}