package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Javid on 2/28/2021.
 */

public interface ImageService {
    void saveImageFile(Long recipeId, MultipartFile imageFile);
}
