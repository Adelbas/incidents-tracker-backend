package ru.adel.locationtracker.public_interface.rest;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IncidentAreaResponse(
        Long id,
        String title,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt
) { }
