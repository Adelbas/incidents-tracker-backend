package ru.adel.locationtracker.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.location.incident.IncidentService;
import ru.adel.locationtracker.core.service.location.user.UserLocationService;
import ru.adel.locationtracker.public_interface.event.EventService;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaRequest;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentGetResponse;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;
import ru.adel.locationtracker.public_interface.rest.IncidentPostRequest;
import ru.adel.locationtracker.public_interface.event.dto.IncidentUserInteractionDto;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationDto;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationMessage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final IncidentService incidentService;

    private final UserLocationService userLocationService;

    private final EventProcessor eventProcessor;

    @Override
    public void handleUserLocationUpdate(UserLocationMessage userLocationMessage) {
        UserLocationDto userLocationDto = userLocationService.saveLocation(userLocationMessage);

        eventProcessor.processUserLocationUpdate(userLocationDto);
    }

    @Override
    public void createIncident(IncidentPostRequest incidentPostRequest) {
        IncidentNotificationDto incident = incidentService.createIncident(incidentPostRequest);

        eventProcessor.processNewIncident(incident);
    }

    @Override
    public IncidentGetResponse getIncident(Long id, LocalDate date) {
        return incidentService.getIncident(id, date);
    }

    @Override
    public void handleUserInteractionUpdate(IncidentUserInteractionDto incidentUserInteractionDto) {
        eventProcessor.processUserInteraction(incidentUserInteractionDto);
    }

    @Override
    public List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest) {
        return incidentService.getIncidentsInArea(incidentAreaRequest);
    }
}
