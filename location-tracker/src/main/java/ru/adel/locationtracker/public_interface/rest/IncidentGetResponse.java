package ru.adel.locationtracker.public_interface.rest;

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
