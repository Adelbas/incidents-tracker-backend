package ru.adel.apigateway.public_interface.incident.dto;

import lombok.Builder;

@Builder
public record IncidentPostRequest(
        String title,
        Double latitude,
        Double longitude,
        byte[] image
) { }
