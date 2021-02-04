package guru.springframework.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)//, exclude = {"notes", "ingredients", "categories"})
@Entity
public class Recipe extends BaseEntity {

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    @Lob
    private String directions;

    @Lob
    private Byte[] image;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public static Builder builder() {
        return new Builder(new HashSet<>(), new HashSet<>());
    }

    @Setter
    @ToString
    @Accessors(fluent = true, chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private String description;
        private Integer prepTime;
        private Integer cookTime;
        private Integer servings;
        private String source;
        private String url;
        private String directions;
        private Byte[] image;
        private Difficulty difficulty;
        private Notes notes;
        private final Set<Ingredient> ingredients;
        private final Set<Category> categories;

        public Builder notes(String notes) {
            this.notes = new Notes();
            this.notes.setRecipeNotes(notes);
            return this;
        }

        public Builder addIngredient(Ingredient ingredient) {
            this.ingredients.add(ingredient);
            return this;
        }

        public Builder addCategory(Category category) {
            this.categories.add(category);
            return this;
        }

        public Recipe build() {
            Recipe recipe  = new Recipe(description, prepTime, cookTime, servings, source,
                    url, directions, image, difficulty, notes, ingredients, categories);

            if (notes != null) {
                notes.setRecipe(recipe);
            }
            ingredients.forEach(ingredient -> ingredient.setRecipe(recipe));
            categories.forEach(category -> category.addRecipe(recipe));

            return recipe;
        }
    }
}
