package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class RecipeCommand {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    @NotNull
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(100)
    @NotNull
    private Integer servings;
    private String source;

    @URL(regexp = "(https?)://(www\\.)?.*\\.(\\D*/?)$")
    private String url;

    @NotBlank
    private String directions;
    private Byte[] image;
    private Difficulty difficulty;
    private NotesCommand notes;
    private Set<IngredientCommand> ingredients = new HashSet<>();
    private Set<CategoryCommand> categories = new HashSet<>();
}
