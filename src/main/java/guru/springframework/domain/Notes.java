package guru.springframework.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

@Entity
public class Notes extends BaseEntity {

    @OneToOne
    private Recipe recipe;

    @Lob
    private String recipeNotes;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getRecipeNotes() {
        return recipeNotes;
    }

    public void setRecipeNotes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }
}
