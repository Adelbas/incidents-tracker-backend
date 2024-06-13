package ru.adel.locationtracker.public_interface.event.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserLocationMessage(
        UUID userId,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
){ }
