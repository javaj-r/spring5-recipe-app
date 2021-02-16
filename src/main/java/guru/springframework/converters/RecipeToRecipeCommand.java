package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        final var command = new RecipeCommand();
        command.setId(recipe.getId());
        command.setDescription(recipe.getDescription());
        command.setPrepTime(recipe.getPrepTime());
        command.setCookTime(recipe.getCookTime());
        command.setServings(recipe.getServings());
        command.setSource(recipe.getSource());
        command.setUrl(recipe.getUrl());
        command.setDirections(recipe.getDirections());
        command.setDifficulty(recipe.getDifficulty());
        command.setNotes(notesConverter.convert(recipe.getNotes()));

        if (recipe.getIngredients() != null) {
            recipe.getIngredients().forEach(
                    ingredient -> command.getIngredients().add(ingredientConverter.convert(ingredient))
            );
        }

        if (recipe.getCategories() != null) {
            recipe.getCategories().forEach(
                    category -> command.getCategories().add(categoryConverter.convert(category))
            );
        }

        return command;
    }
}
