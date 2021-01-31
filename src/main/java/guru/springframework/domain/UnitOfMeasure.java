package guru.springframework.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class UnitOfMeasure extends BaseEntity {

    private String description;

    @OneToMany(mappedBy = "unitOfMeasure", fetch = FetchType.EAGER)
    private Set<Ingredient> ingredients;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public UnitOfMeasure addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }
}
