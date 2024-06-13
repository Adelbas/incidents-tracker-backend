package ru.adel.apigateway.public_interface.incident.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IncidentGetResponse (
        Long id,
        String title,
        UUID postedUserId,
        Double latitude,
        Double longitude,
        Integer views,
        LocalDateTime createdAt
) { }
