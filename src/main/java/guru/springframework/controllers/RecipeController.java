package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("recipe")
public class RecipeController {

    private static final String RECIPE ="recipe";
    private final RecipeService recipeService;

    @GetMapping
    @RequestMapping("{id}/show")
    public String getRecipe(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE, recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/show";
    }

    @GetMapping
    @RequestMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute(RECIPE, new RecipeCommand());

        return "recipe/form";
    }

    @GetMapping
    @RequestMapping("{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE, recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/form";
    }

    @GetMapping
    @RequestMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));

        return "redirect:/";
    }


    @PostMapping
    @RequestMapping(name = "recipe")
    // @RequestMapping(name = "recipe", method = RequestMethod.POST) older way
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        var id = recipeService.saveRecipeCommand(recipeCommand).getId();

        return String.format("redirect:/recipe/%s/show", id);
    }
}
