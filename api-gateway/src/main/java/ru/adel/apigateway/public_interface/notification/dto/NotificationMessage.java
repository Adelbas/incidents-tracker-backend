package ru.adel.apigateway.public_interface.notification.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record NotificationMessage(
        Long incidentId,
        String title,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
) { }
