package ru.adel.locationtracker.core.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.adel.common.kafka.dto.KafkaNotificationMessage;
import ru.adel.locationtracker.core.service.notification.db.IncidentUserInteractionDbService;
import ru.adel.locationtracker.kafka.producer.KafkaNotificationProducer;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaNotificationProducer kafkaNotificationProducer;

    private final IncidentUserInteractionDbService incidentUserInteractionDbService;

    public void notifyUser(UUID userId, List<IncidentNotificationDto> incidents) {
        for (IncidentNotificationDto incident : incidents) {
            kafkaNotificationProducer.send(
                    KafkaNotificationMessage.builder()
                            .userId(userId)
                            .incidentId(incident.id())
                            .title(incident.title())
                            .longitude(incident.longitude())
                            .latitude(incident.latitude())
                            .timestamp(incident.createdAt())
                            .build()
            );
        }
        incidentUserInteractionDbService.updateToNotified(
                userId,
                incidents
        );
    }

    public void notifyUsers(List<UUID> users, IncidentNotificationDto incident) {
        for (UUID userId : users) {
            kafkaNotificationProducer.send(KafkaNotificationMessage.builder()
                    .userId(userId)
                    .incidentId(incident.id())
                    .title(incident.title())
                    .longitude(incident.longitude())
                    .latitude(incident.latitude())
                    .timestamp(incident.createdAt())
                    .build());
        }
        incidentUserInteractionDbService.updateToNotified(
                users,
                incident.id(),
                incident.createdAt()
        );
    }
}