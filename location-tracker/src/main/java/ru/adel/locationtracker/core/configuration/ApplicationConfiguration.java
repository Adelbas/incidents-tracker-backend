package ru.adel.locationtracker.core.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.adel.locationtracker.core.service.analysis.CategorizationProperties;
import ru.adel.locationtracker.core.service.analysis.embedding.EmbeddingProperties;
import ru.adel.locationtracker.core.service.analysis.openai.OpenAiProperties;
import ru.adel.locationtracker.core.service.location.utils.GeoSpatialProperty;

@Configuration
@EnableAsync
@EnableConfigurationProperties({
        GeoSpatialProperty.class,
        EmbeddingProperties.class,
        CategorizationProperties.class,
        OpenAiProperties.class
})
public class ApplicationConfiguration {
}
