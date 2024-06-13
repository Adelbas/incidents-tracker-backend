package ru.adel.apigateway.core.service.incident.client.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.client.incident.rest")
public record IncidentRestClientProperty(String host, int port) {
}
