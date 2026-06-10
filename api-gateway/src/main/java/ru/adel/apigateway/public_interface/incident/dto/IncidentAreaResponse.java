package ru.adel.apigateway.public_interface.incident.dto;

import java.time.LocalDateTime;

public record IncidentAreaResponse(
        Long id,
        String title,
        String categoryCode,
        String categoryName,
        String dangerLevel,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt
) { }
