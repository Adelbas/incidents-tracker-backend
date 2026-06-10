package ru.adel.locationtracker.core.service.analysis;

import ru.adel.locationtracker.public_interface.analysis.DangerLevel;

/**
 * Outcome of categorizing a single event.
 */
public record CategorizationResult(
        Long categoryId,
        String categoryCode,
        String categoryName,
        DangerLevel dangerLevel
) { }
