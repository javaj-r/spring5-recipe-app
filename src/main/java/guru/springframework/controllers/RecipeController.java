package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("recipe")
public class RecipeController {

    private static final String RECIPE = "recipe";
    private final RecipeService recipeService;

    @GetMapping("{id}/show")
    public String getRecipe(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE, recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/show";
    }

    @GetMapping("new")
    public String newRecipe(Model model) {
        model.addAttribute(RECIPE, new RecipeCommand());

        return "recipe/form";
    }

    @GetMapping("{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute(RECIPE, recipeService.findCommandById(Long.valueOf(id)));

        return "recipe/form";
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));

        return "redirect:/";
    }


    @PostMapping(name = "recipe")
    // @RequestMapping(name = "recipe", method = RequestMethod.POST) older way
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand) {
        var id = recipeService.saveRecipeCommand(recipeCommand).getId();

        return String.format("redirect:/recipe/%s/show", id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception e) {
        log.error("Handling not found exception: " + e.getLocalizedMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.getModelMap().addAttribute("title", "404 Recipe Not Found");
        modelAndView.getModelMap().addAttribute("message", "404 Your required recipe not found");

        return modelAndView;
    }
}
