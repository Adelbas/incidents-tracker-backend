package ru.adel.locationtracker.core.service.analysis.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.adel.locationtracker.core.service.analysis.category.db.CategoryRepository;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;
import ru.adel.locationtracker.core.service.analysis.embedding.EmbeddingModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * In-memory cache of active categories together with their precomputed embeddings.
 * <p>
 * Embeddings are recomputed from the category description at startup and after every administrative change,
 * which is what makes the classifier work with a dynamic category set without any retraining.
 * The cache is small (the number of categories) and is replaced atomically on refresh.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryCache {

    private final CategoryRepository categoryRepository;
    private final EmbeddingModel embeddingModel;

    private volatile List<CachedCategory> cache = List.of();

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        try {
            refresh();
        } catch (Exception e) {
            // Do not abort startup if warm-up fails: log the root cause and let the cache be rebuilt later
            // (on the next administrative change). This makes the underlying embedding error visible.
            log.error("Category cache warm-up failed; categorization will be unavailable until the cache is refreshed", e);
        }
    }

    /**
     * Reloads active categories from the database and recomputes their embeddings.
     */
    public synchronized void refresh() {
        List<Category> activeCategories = categoryRepository.findByActiveTrue();
        List<CachedCategory> rebuilt = new ArrayList<>(activeCategories.size());
        for (Category category : activeCategories) {
            // Category descriptions are the "passages" being retrieved -> passage prefix
            float[] embedding = embeddingModel.embedPassage(category.getDescription());
            rebuilt.add(new CachedCategory(category, embedding));
        }
        this.cache = List.copyOf(rebuilt);
        log.info("Category cache refreshed: {} active categories", rebuilt.size());
    }

    /**
     * Returns the category whose embedding is closest (highest cosine similarity) to the query embedding.
     */
    public Optional<Match> nearest(float[] query) {
        return nearest(query, Set.of());
    }

    /**
     * Same as {@link #nearest(float[])} but ignores categories whose code is in {@code excludeCodes}.
     * Used to keep the sentinel "OTHER" category out of the similarity competition.
     */
    public Optional<Match> nearest(float[] query, Set<String> excludeCodes) {
        return rank(query, excludeCodes).stream().findFirst();
    }

    /**
     * Returns all candidate categories (excluding {@code excludeCodes}) ranked by cosine similarity, best first.
     */
    public List<Match> rank(float[] query, Set<String> excludeCodes) {
        List<Match> ranking = new ArrayList<>();
        for (CachedCategory candidate : cache) {
            if (excludeCodes.contains(candidate.category().getCode())) {
                continue;
            }
            ranking.add(new Match(candidate.category(), cosineSimilarity(query, candidate.embedding())));
        }
        ranking.sort((a, b) -> Double.compare(b.score(), a.score()));
        return ranking;
    }

    public Optional<Category> findByCode(String code) {
        return cache.stream()
                .map(CachedCategory::category)
                .filter(category -> category.getCode().equals(code))
                .findFirst();
    }

    public List<Category> activeCategories() {
        return cache.stream().map(CachedCategory::category).toList();
    }

    private static double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) {
            return Double.NEGATIVE_INFINITY;
        }
        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += (double) a[i] * b[i];
            normA += (double) a[i] * a[i];
            normB += (double) b[i] * b[i];
        }
        if (normA == 0.0 || normB == 0.0) {
            return 0.0;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private record CachedCategory(Category category, float[] embedding) { }

    /**
     * A category together with the similarity score that matched it.
     */
    public record Match(Category category, double score) { }
}
