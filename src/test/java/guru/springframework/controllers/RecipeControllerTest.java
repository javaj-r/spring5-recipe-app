package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getRecipe() throws Exception {
        // given
        Recipe recipe = Recipe.builder().id(1L).build();
        when(recipeService.findById(anyLong())).thenReturn(recipe);
        // when
        mockMvc.perform(get("/recipe/1/show"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", instanceOf(Recipe.class)));
    }

    @Test
    void newRecipeForm() throws Exception {
        // given
        // when
        mockMvc.perform(get("/recipe/new"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/form"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", instanceOf(RecipeCommand.class)));
    }

    @Test
    void saveOrUpdateSave() throws Exception {
        // given
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(recipeService.saveRecipeCommand(any())).thenReturn(command);
        // when
        mockMvc.perform(
                post("/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "some string")
        )
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));
    }

    @Test
    void updateRecipeForm() throws Exception {
        // given
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);
        when(recipeService.findCommandById(anyLong())).thenReturn(command);
        // when
        mockMvc.perform(get("/recipe/2/update"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/form"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", instanceOf(RecipeCommand.class)));
    }

    @Test
    void deleteRecipe() throws Exception {
        // given
        // when
        mockMvc.perform(get("/recipe/2/delete"))
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

    }

}
