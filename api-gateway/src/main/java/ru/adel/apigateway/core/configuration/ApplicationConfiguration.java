package ru.adel.apigateway.core.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import ru.adel.apigateway.core.service.incident.client.rest.IncidentRestClientProperty;

@Configuration
@EnableAsync
@EnableConfigurationProperties(IncidentRestClientProperty.class)
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
