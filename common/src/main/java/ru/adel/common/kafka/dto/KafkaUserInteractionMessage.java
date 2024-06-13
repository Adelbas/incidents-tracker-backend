package ru.adel.common.kafka.dto;

import lombok.Builder;
import ru.adel.common.kafka.dto.enums.KafkaInteractionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record KafkaUserInteractionMessage(
        UUID userId,
        Long incidentId,
        LocalDate incidentDate,
        KafkaInteractionStatus status,
        LocalDateTime timestamp
) { }
