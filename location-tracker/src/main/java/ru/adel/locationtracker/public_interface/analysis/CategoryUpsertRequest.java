package ru.adel.locationtracker.public_interface.analysis;

import lombok.Builder;

/**
 * Create/update payload for a category. Used by administrators.
 * <p>
 * Changing the set of categories never requires retraining: the embedding of the affected
 * category is recomputed from {@link #description()} and the in-memory cache is refreshed.
 */
@Builder
public record CategoryUpsertRequest(
        String code,
        String name,
        String description,
        DangerLevel baseDangerLevel,
        Boolean active
) { }
