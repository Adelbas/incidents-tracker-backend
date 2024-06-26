package ru.adel.apigateway.core.service.incident.client.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IncidentRestPostRequest(
        UUID postedUserId,
        String title,
        Double latitude,
        Double longitude,
        byte[] image
) { }
