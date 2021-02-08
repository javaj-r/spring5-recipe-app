package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    IndexController controller;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new IndexController(recipeService);
    }

    @Test
    public void getIndex() {
        String actual = controller.getIndex(model);
        assertEquals("index", actual);
        verify(recipeService, times(1)).getRecipes();
    }

    @Test
    public void getIndex_WhenAddAttribute() {
        // given
        Set<Recipe> recipes;
        Recipe recipe1 = Recipe.builder().build();
        recipe1.setId(1L);
        Recipe recipe2 = Recipe.builder().build();
        recipe1.setId(2L);

        recipes = new HashSet<>();
        recipes.add(recipe1);
        recipes.add(recipe2);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        // when
        controller.getIndex(model);

        // then
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        assertEquals(2, argumentCaptor.getValue().size());
        assertEquals(recipes, argumentCaptor.getValue());
    }

    @Test
    public void testMockMVC() throws Exception {
        // given
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        // when
        mockMvc.perform(get("/"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}