package guru.springframework.services;

import guru.springframework.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Javid on 2/28/2021.
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    @Transactional
    @Override
    public void saveImageFile(Long recipeId, MultipartFile imageFile) {
        recipeRepository.findById(recipeId).ifPresent(
                recipe -> {
                    try {
                        var bytes = imageFile.getBytes();
                        var byteObjects = new Byte[bytes.length];
                        var i = 0;

                        for (var b : bytes) {
                            byteObjects[i++] = b;
                        }

                        recipe.setImage(byteObjects);
                        recipeRepository.save(recipe);

                    } catch (IOException e) {
                        log.error("ImageServiceImpl: saveImageFile: " + e.getMessage());
                    }
                }
        );
    }

}
