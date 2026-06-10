package ru.adel.locationtracker.core.service.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.analysis.category.CategoryCache;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;
import ru.adel.locationtracker.core.service.analysis.embedding.EmbeddingModel;
import ru.adel.locationtracker.core.service.analysis.openai.OpenAiCategoryClient;
import ru.adel.locationtracker.public_interface.analysis.DangerLevel;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Zero-shot categorization pipeline.
 * <ol>
 *     <li>embed the event text with the local ONNX model;</li>
 *     <li>pick the nearest category by cosine similarity;</li>
 *     <li>if the similarity is below the threshold, optionally ask the OpenAI fallback;</li>
 *     <li>otherwise assign the default ({@code OTHER}) category.</li>
 * </ol>
 * The danger level is then derived from the resolved category.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategorizationService {

    private final EmbeddingModel embeddingModel;
    private final CategoryCache categoryCache;
    private final OpenAiCategoryClient openAiCategoryClient;
    private final DangerClassifier dangerClassifier;
    private final CategorizationProperties properties;

    public CategorizationResult categorize(String title, String description) {
        String text = buildText(title, description);
        Category category = resolveCategory(text);
        DangerLevel dangerLevel = dangerClassifier.classify(category);

        return new CategorizationResult(
                category.getId(),
                category.getCode(),
                category.getName(),
                dangerLevel
        );
    }

    private Category resolveCategory(String text) {
        float[] embedding = embeddingModel.embedQuery(text);

        // The default ("OTHER") category is a sentinel/fallback, not a semantic class to match by similarity.
        List<CategoryCache.Match> ranking =
                categoryCache.rank(embedding, Set.of(properties.defaultCategoryCode()));
        logRanking(ranking);

        if (!ranking.isEmpty()) {
            CategoryCache.Match best = ranking.get(0);
            double secondScore = ranking.size() > 1 ? ranking.get(1).score() : Double.NEGATIVE_INFINITY;
            double gap = best.score() - secondScore;

            boolean confident = best.score() >= properties.threshold() && gap >= properties.margin();
            if (confident) {
                log.info("Categorized by embedding similarity: code={}, score={}, margin={}",
                        best.category().getCode(), String.format("%.4f", best.score()), String.format("%.4f", gap));
                return best.category();
            }
            log.info("Embedding not confident (best={} score={}, gap={}, threshold={}, minMargin={})",
                    best.category().getCode(), String.format("%.4f", best.score()), String.format("%.4f", gap),
                    properties.threshold(), properties.margin());
        }

        if (properties.fallbackEnabled()) {
            Optional<String> fallbackCode = openAiCategoryClient.classify(text, categoryCache.activeCategories());
            if (fallbackCode.isPresent()) {
                Optional<Category> fallbackCategory = categoryCache.findByCode(fallbackCode.get());
                if (fallbackCategory.isPresent()) {
                    log.info("Categorized by OpenAI fallback: code={}", fallbackCode.get());
                    return fallbackCategory.get();
                }
            }
        }

        log.info("Assigning default category {}", properties.defaultCategoryCode());
        return defaultCategory();
    }

    private void logRanking(List<CategoryCache.Match> ranking) {
        if (ranking.isEmpty()) {
            log.info("Embedding ranking is empty (no active categories besides the default)");
            return;
        }
        String formatted = ranking.stream()
                .limit(5)
                .map(m -> m.category().getCode() + "=" + String.format("%.4f", m.score()))
                .collect(Collectors.joining(", "));
        log.info("Embedding ranking: {}", formatted);
    }

    private Category defaultCategory() {
        return categoryCache.findByCode(properties.defaultCategoryCode())
                .orElseThrow(() -> new IllegalStateException(
                        "Default category '" + properties.defaultCategoryCode() + "' is not configured (active)"));
    }

    private static String buildText(String title, String description) {
        String safeTitle = title == null ? "" : title.strip();
        if (description == null || description.isBlank()) {
            return safeTitle;
        }
        return safeTitle.isEmpty() ? description.strip() : safeTitle + ". " + description.strip();
    }
}
