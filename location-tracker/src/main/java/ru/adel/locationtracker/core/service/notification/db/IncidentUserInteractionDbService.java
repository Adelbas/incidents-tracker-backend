package ru.adel.locationtracker.core.service.notification.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.notification.db.entity.IncidentUserInteraction;
import ru.adel.locationtracker.core.service.notification.db.entity.IncidentUserInteractionId;
import ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;
import ru.adel.locationtracker.public_interface.event.dto.IncidentUserInteractionDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus.NOTIFIED;
import static ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus.VIEWED;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentUserInteractionDbService {

    private final IncidentUserInteractionRepository incidentUserInteractionRepository;

    private final BatchService batchService;

    public void updateToNotified(List<UUID> users, Long incidentId, LocalDateTime incidentDate) {
        batchService.batchInsert(users, incidentId, incidentDate.toLocalDate(), NOTIFIED);
    }

    public void updateToNotified(UUID userId, List<IncidentNotificationDto> incidents) {
        batchService.batchInsert(userId, incidents, NOTIFIED);
    }

    public void createOrUpdateInteraction(IncidentUserInteractionDto interactionDto) {
        IncidentUserInteractionId id = IncidentUserInteractionId.builder()
                .incidentId(interactionDto.incidentId())
                .incidentDate(interactionDto.incidentDate())
                .userId(interactionDto.userId())
                .build();

        IncidentUserInteraction interaction = incidentUserInteractionRepository.findById(id)
                .map(existingInteraction -> updateInteraction(existingInteraction, interactionDto.timestamp(), interactionDto.status()))
                .orElseGet(() -> createNewInteraction(id, interactionDto.status()));

        incidentUserInteractionRepository.save(interaction);
    }

    private IncidentUserInteraction updateInteraction(IncidentUserInteraction interaction, LocalDateTime timestamp, InteractionStatus newStatus) {
        if (VIEWED.equals(interaction.getStatus()) && NOTIFIED.equals(newStatus)) {
            return interaction;
        }
        if (interaction.getUpdatedAt().isBefore(timestamp)) {
            interaction.setStatus(newStatus);
            interaction.setUpdatedAt(timestamp);
        }
        return interaction;
    }

    private IncidentUserInteraction createNewInteraction(IncidentUserInteractionId id, InteractionStatus interactionStatus) {
        return IncidentUserInteraction.builder()
                .id(id)
                .status(interactionStatus)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
