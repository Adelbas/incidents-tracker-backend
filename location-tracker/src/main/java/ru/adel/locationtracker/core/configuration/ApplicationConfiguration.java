package ru.adel.locationtracker.core.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.adel.locationtracker.core.service.location.utils.GeoSpatialProperty;

@Configuration
@EnableAsync
@EnableConfigurationProperties(GeoSpatialProperty.class)
public class ApplicationConfiguration {
}
