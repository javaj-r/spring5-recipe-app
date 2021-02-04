package guru.springframework.services;

import guru.springframework.domain.Category;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeService recipeService;

    @Mock
    RecipeRepository repository;

    Recipe recipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        HashSet<Recipe> recipes = new HashSet<>();
        recipe = Recipe.builder()
                .description("Description")
                .prepTime(5)
                .cookTime(1)
                .servings(5)
                .source("Source")
                .url("http://www.url.com")
                .directions("Directions")
                .notes("Some Notes")
                .addCategory(new Category())
                .addIngredient(new Ingredient())
                .build();

        recipes.add(recipe);

        when(repository.findAll()).thenReturn(recipes);

        recipeService = new RecipeServiceImpl(repository);
    }

    @Test
    public void getRecipes() {
        Recipe actualRecipe = recipeService.getRecipes().toArray(new Recipe[0])[0];
        assertEquals(recipe, actualRecipe);
        assertNotEquals(new Recipe(), actualRecipe);
        verify(repository, times(1)).findAll();
    }
}