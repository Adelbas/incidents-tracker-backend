package ru.adel.apigateway.core.service.incident.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.incident.client.rest.IncidentRestClient;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentGetResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentPostRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentClientService {

    private final IncidentRestClient incidentRestClient;

    public IncidentGetResponse getIncident(Long id) {
        log.info("Sending get incident with id <{}> request", id);
        return incidentRestClient.getIncident(id);
    }

    public void createIncident(UUID userId, IncidentPostRequest incidentPostRequest) {
        IncidentRestPostRequest request = IncidentRestPostRequest.builder()
                .postedUserId(userId)
                .title(incidentPostRequest.title())
                .latitude(incidentPostRequest.latitude())
                .longitude(incidentPostRequest.longitude())
                .build();
        log.info("Sending create incident request: {}", request);
//        incidentRestClient.createIncident(request);
    }

    public List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest) {
        log.info("Sending get incidents in area request: {}", incidentAreaRequest);
        IncidentAreaResponse[] response = incidentRestClient.getIncidentsInArea(incidentAreaRequest);

        if (response == null || response.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }
}
