package ru.adel.apigateway.core.service.incident.client.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record NotificationDistanceUpdateRequest(
        UUID userId,
        int distance
) { }
