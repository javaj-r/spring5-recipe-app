package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Javid on 3/1/2021.
 */

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveImageFile() throws IOException {
        // given
        Long id = 1L;

        MockMultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt",
                "text/plain", "Fake Image For Testing".getBytes());

        byte[] bytes = multipartFile.getBytes();
        Byte[] byteObjects = new Byte[bytes.length];
        int i = 0;

        for (byte b : bytes) {
            byteObjects[i++] = b;
        }

        Recipe recipe = Recipe.builder().id(id).build();
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        // when
        imageService.saveImageFile(id, multipartFile);
        Recipe savedRecipe = captor.getValue();

        // then
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(captor.capture());
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
        assertArrayEquals(byteObjects, savedRecipe.getImage());

    }
}