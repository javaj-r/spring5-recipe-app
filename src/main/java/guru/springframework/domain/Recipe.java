package guru.springframework.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }


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

        public Builder() {
            this.ingredients = new HashSet<>();
            this.categories = new HashSet<>();
        }

        public Recipe.Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Recipe.Builder setPrepTime(Integer prepTime) {
            this.prepTime = prepTime;
            return this;
        }

        public Recipe.Builder setCookTime(Integer cookTime) {
            this.cookTime = cookTime;
            return this;
        }

        public Recipe.Builder setServings(Integer servings) {
            this.servings = servings;
            return this;
        }

        public Recipe.Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Recipe.Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Recipe.Builder setDirections(String directions) {
            this.directions = directions;
            return this;
        }

        public Recipe.Builder setImage(Byte[] image) {
            this.image = image;
            return this;
        }

        public Recipe.Builder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Recipe.Builder setNotes(Notes notes) {
            this.notes = notes;
            return this;
        }

        public Recipe.Builder setNotes(String notes) {
            this.notes = new Notes();
            this.notes.setRecipeNotes(notes);
            return this;
        }

        public Recipe.Builder addIngredient(Ingredient ingredient) {
            this.ingredients.add(ingredient);
            return this;
        }

        public Recipe.Builder addCategory(Category category) {
            this.categories.add(category);
            return this;
        }

        public Recipe build() {
            Recipe recipe = new Recipe();

            recipe.description = this.description;
            recipe.prepTime = this.prepTime;
            recipe.cookTime = this.cookTime;
            recipe.servings = this.servings;
            recipe.source = this.source;
            recipe.url = this.url;
            recipe.directions = this.directions;
            recipe.image = this.image;
            recipe.difficulty = this.difficulty;
            recipe.notes = this.notes;
            recipe.ingredients = this.ingredients;
            recipe.categories = this.categories;

            notes.setRecipe(recipe);
            ingredients.forEach(ingredient -> ingredient.setRecipe(recipe));
            categories.forEach(category -> category.addRecipe(recipe));

            return recipe;
        }
    }
}
