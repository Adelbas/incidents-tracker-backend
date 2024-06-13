package ru.adel.common.kafka.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record KafkaLocationMessage (
    UUID userId,
    Double latitude,
    Double longitude,
    LocalDateTime timestamp
) { }
