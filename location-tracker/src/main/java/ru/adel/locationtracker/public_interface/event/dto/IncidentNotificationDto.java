package ru.adel.locationtracker.public_interface.event.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IncidentNotificationDto(
        Long id,
        String title,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt
) { }
