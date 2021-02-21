package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToCommand;
    private final IngredientCommandToIngredient commandToIngredient;

    public IngredientServiceImplTest() {
        this.ingredientToCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.commandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(recipeRepository, unitRepository,
                ingredientToCommand, commandToIngredient);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        // given
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        Recipe recipe = Recipe.builder()
                .id(1L)
                .addIngredient(ingredient1)
                .addIngredient(ingredient2)
                .addIngredient(ingredient3)
                .build();

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        // when
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        // then
        assertEquals(Long.valueOf(3L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        verify(recipeRepository).findById(anyLong());
    }

    @Test
    void saveIngredientCommand() {
        // given
        IngredientCommand command = new IngredientCommand();
        command.setId(1L);
        command.setRecipeId(2L);

        Recipe recipe = Recipe.builder().id(2L).addIngredient(new Ingredient()).build();
        recipe.getIngredients().iterator().next().setId(1L);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(new Recipe()));
        when(recipeRepository.save(any())).thenReturn(recipe);

        // when
        IngredientCommand actualCommand = ingredientService.saveIngredientCommand(command);

        // then
        assertNotNull(actualCommand);
        assertEquals(Long.valueOf(1L), actualCommand.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any());


    }

}