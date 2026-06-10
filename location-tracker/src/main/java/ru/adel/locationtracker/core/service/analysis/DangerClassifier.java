package ru.adel.locationtracker.core.service.analysis;

import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;
import ru.adel.locationtracker.public_interface.analysis.DangerLevel;

/**
 * Determines the danger level of an event.
 * <p>
 * Iteration 1 derives the level from the category's configured base level. Iteration 2 will add
 * escalation driven by spatio-temporal clustering (e.g. many reports of the same event raise the level).
 */
@Service
public class DangerClassifier {

    public DangerLevel classify(Category category) {
        return category.getBaseDangerLevel();
    }
}
