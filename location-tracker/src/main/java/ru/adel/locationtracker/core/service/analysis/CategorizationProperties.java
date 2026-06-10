package ru.adel.locationtracker.core.service.analysis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Tuning of the categorization pipeline.
 * <p>
 * An embedding match is accepted only when it is both confident in absolute terms (>= {@code threshold})
 * and clearly ahead of the runner-up (top1 - top2 >= {@code margin}). E5-family models compress cosine
 * scores into a narrow high band, so the relative margin is what actually separates a real match from a
 * coincidental lexical overlap; anything below either bar is routed to the OpenAI fallback.
 *
 * @param threshold           minimum cosine similarity of the best category (default {@code 0.80})
 * @param margin              minimum gap between the best and the second-best category (default {@code 0.03})
 * @param fallbackEnabled     whether to call the OpenAI fallback on low-confidence matches
 * @param defaultCategoryCode category assigned when nothing else matches (default {@code OTHER})
 */
@ConfigurationProperties(prefix = "categorization")
public record CategorizationProperties(
        double threshold,
        double margin,
        boolean fallbackEnabled,
        String defaultCategoryCode
) {
    public CategorizationProperties {
        if (threshold <= 0.0) {
            threshold = 0.80;
        }
        if (margin <= 0.0) {
            margin = 0.03;
        }
        if (defaultCategoryCode == null || defaultCategoryCode.isBlank()) {
            defaultCategoryCode = "OTHER";
        }
    }
}
