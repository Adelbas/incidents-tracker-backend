package ru.adel.locationtracker.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.location.incident.IncidentService;
import ru.adel.locationtracker.core.service.location.user.UserLocationService;
import ru.adel.locationtracker.core.service.notification.NotificationService;
import ru.adel.locationtracker.core.service.notification.db.IncidentUserInteractionDbService;
import ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;
import ru.adel.locationtracker.public_interface.event.dto.IncidentUserInteractionDto;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationDto;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventProcessor {

    private final IncidentService incidentService;

    private final UserLocationService userLocationService;

    private final NotificationService notificationService;

    private final IncidentUserInteractionDbService incidentUserInteractionDbService;

    @Async
    public void processNewIncident(IncidentNotificationDto incident) {
        List<UUID> users = userLocationService.getUsersToNotifyForCoordinates(
                incident.longitude(), incident.latitude()
        );
        notificationService.notifyUsers(users, incident);
    }

    @Async
    public void processUserLocationUpdate(UserLocationDto userLocationDto) {
        List<IncidentNotificationDto> incidents = incidentService.getIncidentsNearbyUserLocation(userLocationDto);
        notificationService.notifyUser(userLocationDto.userId(), incidents);
    }

    @Async
    public void processUserInteraction(IncidentUserInteractionDto incidentUserInteractionDto) {
        incidentUserInteractionDbService.createOrUpdateInteraction(incidentUserInteractionDto);

        if (incidentUserInteractionDto.status().equals(InteractionStatus.VIEWED)) {
            incidentService.incrementViews(incidentUserInteractionDto.incidentId());
        }
    }
}
