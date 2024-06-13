package ru.adel.locationtracker.public_interface.event.dto;

import lombok.Builder;
import ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record IncidentUserInteractionDto(
        UUID userId,
        Long incidentId,
        LocalDate incidentDate,
        InteractionStatus status,
        LocalDateTime timestamp
) { }
