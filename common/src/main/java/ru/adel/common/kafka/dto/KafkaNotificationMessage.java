package ru.adel.common.kafka.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record KafkaNotificationMessage(
        UUID userId,
        Long incidentId,
        String title,
        String categoryCode,
        String categoryName,
        String dangerLevel,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
) { }
