package ru.adel.locationtracker.public_interface.rest;

import lombok.Builder;
import ru.adel.locationtracker.public_interface.analysis.DangerLevel;

import java.time.LocalDateTime;

@Builder
public record IncidentAreaResponse(
        Long id,
        String title,
        String categoryCode,
        String categoryName,
        DangerLevel dangerLevel,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt
) { }
