package guru.springframework.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category extends BaseEntity {

    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private Set<Recipe> recipes = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public Category addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        return this;
    }
}
