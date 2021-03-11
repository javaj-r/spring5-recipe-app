package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Javid on 3/10/2021.
 */

@RequiredArgsConstructor
@Component
@Profile({"dev", "prod"})
public class MySQLBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (categoryRepository.count() == 0L) {
            loadCategories("American", "Italian", "Chinese", "Mexican", "Japanese", "Fast Food");
        }

        if (unitOfMeasureRepository.count() == 0L) {
            loadUnitOfMeasures("Each", "Teaspoon", "Tablespoon", "Cup", "Pinch", "Ounce", "Pint", "Dash");
        }
    }

    private void loadCategories(String... descriptions) {
        for (var desc : descriptions) {
            var category = new Category();
            category.setDescription(desc);
            categoryRepository.save(category);
        }
    }

    private void loadUnitOfMeasures(String... descriptions) {
        for (var desc : descriptions) {
            var unitOfMeasure = new UnitOfMeasure();
            unitOfMeasure.setDescription(desc);
            unitOfMeasureRepository.save(unitOfMeasure);
        }
    }
}
