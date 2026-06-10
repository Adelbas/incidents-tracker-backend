package ru.adel.locationtracker.core.service.analysis.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration of the external OpenAI fallback used only for low-confidence categorization.
 *
 * @param enabled   master switch for the fallback
 * @param apiKey    OpenAI API key; when blank the client silently degrades (returns no suggestion)
 * @param baseUrl   API base url (default {@code https://api.openai.com/v1})
 * @param model     chat-completions model (default {@code gpt-4o-mini})
 * @param timeoutMs connect/read timeout in milliseconds (default {@code 4000})
 */
@ConfigurationProperties(prefix = "openai")
public record OpenAiProperties(
        boolean enabled,
        String apiKey,
        String baseUrl,
        String model,
        Integer timeoutMs
) {
    public OpenAiProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://api.openai.com/v1";
        }
        if (model == null || model.isBlank()) {
            model = "gpt-4o-mini";
        }
        if (timeoutMs == null || timeoutMs <= 0) {
            timeoutMs = 4000;
        }
    }

    public boolean isUsable() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }
}
