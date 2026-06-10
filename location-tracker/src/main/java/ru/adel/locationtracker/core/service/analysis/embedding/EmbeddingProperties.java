package ru.adel.locationtracker.core.service.analysis.embedding;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration of the external embedding service (runs the e5 model in its own container).
 *
 * @param url           base URL of the embedding service (e.g. {@code http://embedding-service:8000})
 * @param timeoutMs     connect/read timeout in milliseconds (default {@code 5000})
 * @param queryPrefix   prefix for the event text being classified. E5 is asymmetric and expects {@code "query: "}.
 * @param passagePrefix prefix for the category descriptions. E5 expects {@code "passage: "} here. Using the
 *                      correct asymmetric prefixes is essential for good separation.
 */
@ConfigurationProperties(prefix = "embedding.service")
public record EmbeddingProperties(
        String url,
        Integer timeoutMs,
        String queryPrefix,
        String passagePrefix
) {
    public EmbeddingProperties {
        if (url == null || url.isBlank()) {
            url = "http://localhost:8000";
        }
        if (timeoutMs == null || timeoutMs <= 0) {
            timeoutMs = 5000;
        }
        if (queryPrefix == null) {
            queryPrefix = "query: ";
        }
        if (passagePrefix == null) {
            passagePrefix = "passage: ";
        }
    }
}
