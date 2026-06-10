package ru.adel.locationtracker.core.service.analysis.embedding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Client to the external embedding service that runs the e5 model in its own container.
 * <p>
 * Returns L2-normalized embeddings (normalization is done by the service), so the cosine similarity between
 * two embeddings equals their dot product. The public API ({@link #embedQuery}/{@link #embedPassage}) is the
 * same as the previous in-JVM implementation, so the rest of the pipeline is unchanged.
 */
@Slf4j
@Component
public class EmbeddingModel {

    private final EmbeddingProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmbeddingModel(EmbeddingProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.timeoutMs());
        factory.setReadTimeout(properties.timeoutMs());
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Encodes the event text to classify (uses the {@code query:} prefix).
     */
    public float[] embedQuery(String text) {
        return embed(properties.queryPrefix(), text);
    }

    /**
     * Encodes a category description (uses the {@code passage:} prefix).
     */
    public float[] embedPassage(String text) {
        return embed(properties.passagePrefix(), text);
    }

    private float[] embed(String prefix, String text) {
        String input = prefix + (text == null ? "" : text.strip());
        String url = properties.url() + "/embed";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = Map.of("inputs", List.of(input));

            String response = restTemplate.postForObject(url, new HttpEntity<>(body, headers), String.class);
            return parseFirstEmbedding(response);
        } catch (EmbeddingException e) {
            throw e;
        } catch (RestClientResponseException e) {
            // Include the embedding service response body so the real cause is visible in the logs
            throw new EmbeddingException(
                    "Embedding service returned " + e.getStatusCode() + " from " + url + ": " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new EmbeddingException("Failed to compute embedding via " + url, e);
        }
    }

    private float[] parseFirstEmbedding(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode vector = root.path("embeddings").path(0);
            if (!vector.isArray() || vector.isEmpty()) {
                throw new EmbeddingException("Unexpected embedding service response: " + response);
            }
            float[] result = new float[vector.size()];
            for (int i = 0; i < vector.size(); i++) {
                result[i] = (float) vector.get(i).asDouble();
            }
            return result;
        } catch (EmbeddingException e) {
            throw e;
        } catch (Exception e) {
            throw new EmbeddingException("Failed to parse embedding service response", e);
        }
    }
}
