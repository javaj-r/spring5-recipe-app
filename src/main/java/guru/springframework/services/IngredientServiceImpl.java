package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    @NonNull
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        return recipeRepository.findById(recipeId)
                .orElseThrow(RuntimeException::new)
                .getIngredients()
                .parallelStream()
                .filter(v -> v.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst()
                .orElse(null);
    }

}
