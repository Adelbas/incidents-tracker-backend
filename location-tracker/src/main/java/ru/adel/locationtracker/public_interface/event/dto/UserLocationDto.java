package ru.adel.locationtracker.public_interface.event.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserLocationDto(
        UUID userId,
        Double longitude,
        Double latitude,
        Integer notificationDistance
) { }
