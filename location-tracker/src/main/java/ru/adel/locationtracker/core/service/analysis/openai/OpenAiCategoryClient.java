package ru.adel.locationtracker.core.service.analysis.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Fallback categorizer backed by the OpenAI chat-completions API.
 * <p>
 * It is invoked only when the embedding-based classifier is not confident enough. The model is asked to
 * pick exactly one category code from the supplied list. Any failure (disabled, missing key, timeout,
 * malformed answer) returns {@link Optional#empty()} so the caller can fall back to the default category.
 */
@Slf4j
@Component
public class OpenAiCategoryClient {

    private final OpenAiProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAiCategoryClient(OpenAiProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.timeoutMs());
        factory.setReadTimeout(properties.timeoutMs());
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * @return the chosen category code, or empty when no confident/valid suggestion is available
     */
    public Optional<String> classify(String text, List<Category> categories) {
        if (!properties.isUsable() || categories.isEmpty()) {
            return Optional.empty();
        }
        try {
            Map<String, Object> body = buildRequestBody(text, categories);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(properties.apiKey());

            String url = properties.baseUrl() + "/chat/completions";
            String response = restTemplate.postForObject(
                    url,
                    new org.springframework.http.HttpEntity<>(body, headers),
                    String.class
            );
            return parseCode(response, categories);
        } catch (Exception e) {
            log.warn("OpenAI fallback categorization failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Map<String, Object> buildRequestBody(String text, List<Category> categories) {
        String categoryList = categories.stream()
                .map(c -> "- " + c.getCode() + ": " + c.getName() + " — " + c.getDescription())
                .collect(Collectors.joining("\n"));

        String system = "Ты классификатор негативных событий. Тебе дан список категорий (код: название — описание). "
                + "Верни ТОЛЬКО код одной наиболее подходящей категории из списка, без пояснений и знаков препинания.";
        String user = "Категории:\n" + categoryList + "\n\nТекст события:\n" + text + "\n\nКод категории:";

        Map<String, Object> systemMessage = new LinkedHashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", system);

        Map<String, Object> userMessage = new LinkedHashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", user);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", properties.model());
        body.put("temperature", 0);
        body.put("max_tokens", 16);
        body.put("messages", List.of(systemMessage, userMessage));
        return body;
    }

    private Optional<String> parseCode(String response, List<Category> categories) throws Exception {
        if (response == null) {
            return Optional.empty();
        }
        JsonNode root = objectMapper.readTree(response);
        JsonNode content = root.path("choices").path(0).path("message").path("content");
        if (content.isMissingNode()) {
            return Optional.empty();
        }
        String answer = content.asText().trim().toUpperCase();
        return categories.stream()
                .map(Category::getCode)
                .filter(code -> code.equalsIgnoreCase(answer))
                .findFirst();
    }
}
