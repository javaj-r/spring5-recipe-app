package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    @Override
    public Set<Recipe> findAll() {
        log.debug("RecipeServiceImpl: findAll");
        Set<Recipe> recipes = new HashSet<>();
        repository.findAll().forEach(recipes::add);
        return recipes;
    }

    @Override
    public Recipe findById(Long id) {
        log.debug("RecipeServiceImpl: findById");
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Recipe Not Found!"));
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Long id) {
        log.debug("RecipeServiceImpl: findCommandById");
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        log.debug("RecipeServiceImpl: saveRecipeCommand");

        var recipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe saveRecipe = recipe != null ? repository.save(recipe) : null;
        log.debug(saveRecipe != null ? "Saved RecipeId: " + saveRecipe.getId() : "Saved Recipe is null!");

        return recipeToRecipeCommand.convert(saveRecipe);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
