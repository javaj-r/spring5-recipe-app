package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAll() {
        // given
        HashSet<Recipe> recipes = new HashSet<>();
        recipes.add(Recipe.builder().build());
        when(repository.findAll()).thenReturn(recipes);
        // when
        Set<Recipe> actualRecipe = recipeService.findAll();
        // then
        assertEquals(1, actualRecipe.size());
        verify(repository).findAll();
    }

    @Test
    void findById() {
        // given
        Recipe recipe = Recipe.builder().id(1L).build();
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(repository.findById(anyLong())).thenReturn(recipeOptional);
        // when
        Recipe actualRecipe = recipeService.findById(1L);
        // then
        assertNotNull(actualRecipe, "Null recipe returned");
        verify(repository).findById(anyLong());
        verify(repository, never()).findAll();
    }

    @Test
    void findByIdNotFound() {
        // given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        Executable executable = () -> recipeService.findById(anyLong());
        // then
        assertThrows(RuntimeException.class, executable);
        verify(repository).findById(anyLong());
        verify(repository, never()).findAll();
    }

    @Test
    void deleteById() {
        // given
        Long deleteId = 2L;
        // when
        recipeService.deleteById(deleteId);
        // then
        verify(repository).deleteById(anyLong());
    }
}