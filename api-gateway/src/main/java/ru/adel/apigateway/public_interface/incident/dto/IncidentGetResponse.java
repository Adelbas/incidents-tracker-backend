package ru.adel.apigateway.public_interface.incident.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IncidentGetResponse (
        Long id,
        String title,
        UUID postedUserId,
        String postedUserFirstName,
        String postedUserLastName,
        Double latitude,
        Double longitude,
        byte[] image,
        Integer views,
        LocalDateTime createdAt
) { }
