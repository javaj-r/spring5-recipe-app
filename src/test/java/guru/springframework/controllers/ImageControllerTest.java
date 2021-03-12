package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Javid on 2/28/2021.
 */

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    @InjectMocks
    ImageController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    void getImageForm() throws Exception {
        // given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        // when
        mockMvc.perform(get("/recipe/1/image/upload"))
                // then
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/image/form"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void handleImagePost() throws Exception {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile", "fakeImage.txt",
                "text/plain", "Fake Image File String Content".getBytes());
        // when
        mockMvc.perform(multipart("/recipe/1/image/upload").file(multipartFile))
                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService).saveImageFile(anyLong(), any());
    }

    @Test
    void renderImageFromDatabase() throws Exception {
        // given
        byte[] bytes = "fake image text".getBytes();
        Byte[] imageBytes = new Byte[bytes.length];
        int i = 0;

        for (byte b : bytes) {
            imageBytes[i++] = b;
        }

        RecipeCommand command = new RecipeCommand();
        command.setId(1L);
        command.setImage(imageBytes);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        // when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        assertEquals(bytes.length, response.getContentAsByteArray().length);
        assertArrayEquals(bytes, response.getContentAsByteArray());
    }


    @Test
    void getRecipeIsNotFoundException() throws Exception {
        // given
        when(recipeService.findCommandById(anyLong())).thenThrow(NotFoundException.class);
        // when
        mockMvc.perform(get("/recipe/1/image/upload"))
                // then
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404error"));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void getRecipeNumberFormatException() throws Exception {
        // given
        // when
        mockMvc.perform(get("/recipe/badId/image/upload"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error/400error"));
    }

}
