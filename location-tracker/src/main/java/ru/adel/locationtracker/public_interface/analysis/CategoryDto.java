package ru.adel.locationtracker.public_interface.analysis;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Read model of a category exposed through the admin REST API.
 */
@Builder
public record CategoryDto(
        Long id,
        String code,
        String name,
        String description,
        DangerLevel baseDangerLevel,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
