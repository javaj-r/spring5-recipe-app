package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Javid on 2/28/2021.
 */

@RequiredArgsConstructor
@Controller
@RequestMapping("recipe/{recipeId}/image")
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;
    ;

    @GetMapping("upload")
    public String getImageForm(@PathVariable Long recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipe/image/form";
    }

    @PostMapping("upload")
    public String handleImagePost(@PathVariable Long recipeId, @RequestParam("imageFile") MultipartFile imageFile) {
        imageService.saveImageFile(recipeId, imageFile);

        return "redirect:/recipe/{recipeId}/show";
    }

    @GetMapping
    public void renderImageFromDatabase(@PathVariable Long recipeId, HttpServletResponse response) throws IOException {
        var image = recipeService.findCommandById(recipeId).getImage();
        if (image != null) {
            var bytes = new byte[image.length];
            var i = 0;

            for (var b : image) {
                bytes[i++] = b;
            }

            response.setContentType("image/jpeg");
            var inputStream = new ByteArrayInputStream(bytes);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

}
