package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final UnitOfMeasureToUnitOfMeasureCommand converter;

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        final var command = new IngredientCommand();
        command.setId(ingredient.getId());
        if (ingredient.getRecipe() != null) {
            command.setRecipeId(ingredient.getRecipe().getId());
        }
        command.setAmount(ingredient.getAmount());
        command.setDescription(ingredient.getDescription());
        command.setUnitOfMeasure(converter.convert(ingredient.getUnitOfMeasure()));
        return command;
    }
}
