package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitRepository;
    private final IngredientToIngredientCommand ingredientToCommand;
    private final IngredientCommandToIngredient commandToIngredient;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        return recipeRepository.findById(recipeId)
                .orElseThrow(RuntimeException::new)
                .getIngredients()
                .parallelStream()
                .filter(v -> v.getId().equals(ingredientId))
                .map(ingredientToCommand::convert)
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        final var cM = "IngredientService: saveIngredientCommand: ";

        if (command == null) {
            log.debug(cM + "Null IngredientCommand");
            return null;
        }

        var optionalRecipe = recipeRepository.findById(command.getRecipeId());
        var recipe = optionalRecipe.orElseThrow(() -> new RuntimeException(cM + "Recipe Not Found!!"));

        var optionalIngredient = recipe.getIngredients()
                .stream()
                .filter(i -> Objects.equals(i.getId(), command.getId()))
                .findFirst();

        optionalIngredient.ifPresentOrElse(
                ingredient -> {
                    ingredient.setDescription(command.getDescription());
                    ingredient.setAmount(command.getAmount());
                    ingredient.setUnitOfMeasure(unitRepository
                            .findById(command.getUnitOfMeasure().getId())
                            .orElseThrow(() -> new RuntimeException(cM + "UnitOfMeasure Not Found!!"))
                    );
                },
                /* else */
                () -> recipe.addIngredients(commandToIngredient.convert(command))
        );

        var savedRecipe = recipeRepository.save(recipe);

        if (command.getId() != null) {
            return savedRecipe.getIngredients().stream()
                    .filter(i -> Objects.equals(i.getId(), command.getId()))
                    .findFirst()
                    .map(ingredientToCommand::convert)
                    .orElseThrow(() -> new RuntimeException(cM + "Ingredient Not Found!!"));
        }

        return savedRecipe.getIngredients().stream()
                .filter(i -> Objects.equals(i.getDescription(), command.getDescription()))
                .filter(i -> Objects.equals(i.getAmount(), command.getAmount()))
                .filter(i -> Objects.equals(i.getUnitOfMeasure().getId(), command.getUnitOfMeasure().getId()))
                .findFirst()
                .map(ingredientToCommand::convert)
                .orElseThrow(() -> new RuntimeException(cM + "Ingredient Not Found!!"));
    }

    @Override
    @Transactional
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        recipeRepository.findById(recipeId).ifPresent(
                recipe -> recipe.getIngredients().stream()
                        .filter(i -> Objects.equals(i.getId(), ingredientId))
                        .findFirst()
                        .ifPresent(i -> {
                                    i.setRecipe(null);
                                    recipe.getIngredients().remove(i);
                                    recipeRepository.save(recipe);
                                    log.debug("delete ingredient id: " + ingredientId);
                                }
                        )
        );

    }

}
