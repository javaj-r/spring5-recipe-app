package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @RequestMapping("{id}/show")
    public String getRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));

        return "recipe/show";
    }

    @RequestMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/form";
    }

    @RequestMapping("{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/form";
    }


    @PostMapping
    @RequestMapping(name = "recipe")
    // @RequestMapping(name = "recipe", method = RequestMethod.POST) older way
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        var id = recipeService.saveRecipeCommand(recipeCommand).getId();

        return String.format("redirect:/recipe/%s/show", id);
    }
}
