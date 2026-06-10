package ru.adel.locationtracker.public_interface.rest;

import lombok.Builder;
import ru.adel.locationtracker.public_interface.analysis.DangerLevel;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IncidentGetResponse (
        Long id,
        String title,
        String description,
        String categoryCode,
        String categoryName,
        DangerLevel dangerLevel,
        UUID postedUserId,
        Double latitude,
        Double longitude,
        byte[] image,
        Integer views,
        LocalDateTime createdAt
) { }
