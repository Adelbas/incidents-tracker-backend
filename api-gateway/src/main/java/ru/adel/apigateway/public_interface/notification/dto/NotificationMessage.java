package ru.adel.apigateway.public_interface.notification.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationMessage(
        Long incidentId,
        String title,
        String categoryCode,
        String categoryName,
        String dangerLevel,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
) { }
