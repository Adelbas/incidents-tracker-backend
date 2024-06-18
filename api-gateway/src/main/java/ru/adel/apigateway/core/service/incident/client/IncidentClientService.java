package ru.adel.apigateway.core.service.incident.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.incident.client.dto.IncidentResponse;
import ru.adel.apigateway.core.service.incident.client.dto.IncidentRestPostRequest;
import ru.adel.apigateway.core.service.incident.client.dto.NotificationDistanceUpdateRequest;
import ru.adel.apigateway.core.service.incident.client.rest.IncidentRestClient;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentPostRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentClientService {

    private final IncidentRestClient incidentRestClient;

    public IncidentResponse getIncident(Long id, LocalDate date) {
        log.info("Sending get incident with id <{}> date <{}> request", id, date);
        return incidentRestClient.getIncident(id, date);
    }

    @Async
    public void createIncident(UUID userId, IncidentPostRequest incidentPostRequest) {
        IncidentRestPostRequest request = IncidentRestPostRequest.builder()
                .postedUserId(userId)
                .title(incidentPostRequest.title())
                .latitude(incidentPostRequest.latitude())
                .longitude(incidentPostRequest.longitude())
                .image(incidentPostRequest.image())
                .build();
        log.info("Sending create incident request, image size = {}, : {}",request.image().length, request);
        incidentRestClient.createIncident(request);
    }

    public List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest) {
        log.info("Sending get incidents in area request: {}", incidentAreaRequest);
        IncidentAreaResponse[] response = incidentRestClient.getIncidentsInArea(incidentAreaRequest);

        if (response == null || response.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(response);
    }

    @Async
    public void updateNotificationDistance(UUID userId, int distance) {
        log.info("Sending update notification distance to <{}> for user <{}> request", distance, userId);
        incidentRestClient.updateNotificationDistance(
                NotificationDistanceUpdateRequest.builder()
                        .userId(userId)
                        .distance(distance)
                        .build()
        );
    }
}
