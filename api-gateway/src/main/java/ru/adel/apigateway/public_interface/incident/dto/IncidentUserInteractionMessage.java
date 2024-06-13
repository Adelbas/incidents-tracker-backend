package ru.adel.apigateway.public_interface.incident.dto;

import lombok.Builder;
import ru.adel.apigateway.public_interface.incident.dto.enums.InteractionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record IncidentUserInteractionMessage(
        Long incidentId,
        LocalDate incidentDate,
        InteractionStatus status,
        LocalDateTime timestamp
) { }
