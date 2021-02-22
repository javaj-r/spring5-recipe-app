package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    @InjectMocks
    IngredientController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getIngredients() throws Exception {
        // given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        when(recipeService.findCommandById(anyLong())).thenReturn(command);
        // when
        mockMvc.perform(get("/recipe/1/ingredients"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attribute("recipe", instanceOf(RecipeCommand.class)));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void getIngredient() throws Exception {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(2L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        // when
        mockMvc.perform(get("/recipe/1/ingredient/1/show"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attribute("ingredient", instanceOf(IngredientCommand.class)));
    }

    @Test
    void newIngredientForm() throws Exception {
        // given
        when(unitOfMeasureService.findAllCommands()).thenReturn(new HashSet<>());

        // when
        mockMvc.perform(get("/recipe/2/ingredient/new"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/form"))
                .andExpect(model().attributeExists("ingredient", "units"))
                .andExpect(model().attribute("ingredient", instanceOf(IngredientCommand.class)))
                .andExpect(model().attribute("units", instanceOf(Set.class)));

        verify(unitOfMeasureService).findAllCommands();
    }

    @Test
    void updateIngredientForm() throws Exception {
        // given
        IngredientCommand command = new IngredientCommand();
        command.setId(2L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(command);
        when(unitOfMeasureService.findAllCommands()).thenReturn(new HashSet<>());

        // when
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/form"))
                .andExpect(model().attributeExists("ingredient", "units"))
                .andExpect(model().attribute("ingredient", instanceOf(IngredientCommand.class)))
                .andExpect(model().attribute("units", instanceOf(Set.class)));
    }

    @Test
    void saveOrUpdatePost() throws Exception {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);
        ingredientCommand.setRecipeId(2L);
        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);
        // when
        mockMvc.perform(
                post("/ingredient")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1")
                        .param("recipeId", "2")
        )
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/1/show"));

    }

    @Test
    void deleteIngredient() throws Exception {
        // given
        // when
        mockMvc.perform(get("/recipe/1/ingredient/2/delete"))
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService).deleteByRecipeIdAndIngredientId(anyLong(), anyLong());
    }

}
