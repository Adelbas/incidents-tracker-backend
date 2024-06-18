package ru.adel.apigateway.core.service.incident.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record IncidentResponse(
        Long id,
        String title,
        UUID postedUserId,
        Double latitude,
        Double longitude,
        byte[] image,
        Integer views,
        LocalDateTime createdAt
) { }
