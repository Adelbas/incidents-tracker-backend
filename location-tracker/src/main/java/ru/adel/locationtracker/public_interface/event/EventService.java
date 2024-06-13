package ru.adel.locationtracker.public_interface.event;

import ru.adel.locationtracker.public_interface.rest.IncidentAreaRequest;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentGetResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentPostRequest;
import ru.adel.locationtracker.public_interface.event.dto.IncidentUserInteractionDto;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationMessage;

import java.util.List;

public interface EventService {

    void handleUserLocationUpdate(UserLocationMessage userLocationMessage);

    void createIncident(IncidentPostRequest incidentPostRequest);

    IncidentGetResponse getIncident(Long id);

    void handleUserInteractionUpdate(IncidentUserInteractionDto incidentUserInteractionDto);

    List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest);
}
