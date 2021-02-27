package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IngredientController {

    private static final String INGREDIENT = "ingredient";

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping("recipe/{recipeId}/ingredients")
    public String getIngredients(@PathVariable String recipeId, Model model) {
        var command = recipeService.findCommandById(Long.valueOf(recipeId));
        log.debug(String.valueOf(command.getIngredients().size()));
        model.addAttribute("recipe", command);

        return "recipe/ingredient/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String getIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute(
                INGREDIENT,
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId))
        );

        return "recipe/ingredient/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        var ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute(INGREDIENT, ingredientCommand);
        model.addAttribute("units", unitOfMeasureService.findAllCommands());

        return "recipe/ingredient/form";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute(
                INGREDIENT,
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId))
        );

        model.addAttribute("units", unitOfMeasureService.findAllCommands());

        return "recipe/ingredient/form";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {
        ingredientService.deleteByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId));

        return String.format("redirect:/recipe/%s/ingredients", recipeId);
    }

    @PostMapping(INGREDIENT)
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        var savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);
        var recipeId = savedIngredientCommand.getRecipeId();
        var id = savedIngredientCommand.getId();

        return String.format("redirect:/recipe/%s/ingredient/%s/show", recipeId, id);
    }
}
