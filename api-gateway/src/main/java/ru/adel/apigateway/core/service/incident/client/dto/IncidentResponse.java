package ru.adel.apigateway.core.service.incident.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record IncidentResponse(
        Long id,
        String title,
        String description,
        String categoryCode,
        String categoryName,
        String dangerLevel,
        UUID postedUserId,
        Double latitude,
        Double longitude,
        byte[] image,
        Integer views,
        LocalDateTime createdAt
) { }
