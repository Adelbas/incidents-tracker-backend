package ru.adel.apigateway.public_interface.incident;

import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentGetResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentPostRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentUserInteractionMessage;

import java.util.List;
import java.util.UUID;

public interface IncidentService {

    void handleUserInteraction(UUID userId, IncidentUserInteractionMessage interactionMessage);

    IncidentGetResponse getIncident(Long id);

    void createIncident(UUID userId, IncidentPostRequest incidentPostRequest);

    List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest);
}
