package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients")
    public String getIngredients(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String getIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute(
                "ingredient",
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId))
        );

        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        model.addAttribute(
                "ingredient",
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId))
        );

        model.addAttribute("units", unitOfMeasureService.findAllCommands());

        return "recipe/ingredient/form";
    }

    @PostMapping
    @RequestMapping("ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
        var savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);
        var recipeId = savedIngredientCommand.getRecipeId();
        var id = savedIngredientCommand.getId();

        return String.format("redirect:/recipe/%s/ingredient/%s/show", recipeId, id);
    }
}
