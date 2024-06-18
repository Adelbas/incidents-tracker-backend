package ru.adel.apigateway.core.service.incident.client.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.adel.apigateway.core.service.incident.client.dto.IncidentResponse;
import ru.adel.apigateway.core.service.incident.client.dto.IncidentRestPostRequest;
import ru.adel.apigateway.core.service.incident.client.dto.NotificationDistanceUpdateRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;

import java.time.LocalDate;

@Slf4j
@Component
public class IncidentRestClient {

    private final String basePath;

    private final RestTemplate restTemplate;

    public IncidentRestClient(IncidentRestClientProperty property, RestTemplate restTemplate) {
        this.basePath = "http://" + property.host() + ":" + property.port();
        this.restTemplate = restTemplate;
    }

    public IncidentResponse getIncident(Long id, LocalDate date) {
        try {
            String url = basePath + "/api/incident/" + id + "?date=" + date;
            return restTemplate.getForObject(url, IncidentResponse.class);
        } catch (Exception e) {
            log.error("Got error by get incident <{}> <{}> request: ", id, date, e);
            throw e;
        }
    }

    public void createIncident(IncidentRestPostRequest request) {
        try {
            String url = basePath + "/api/incident";
            restTemplate.postForEntity(url, request, Void.class);
        } catch (Exception e) {
            log.error("Got error by create incident request: {}", request , e);
            throw e;
        }
    }

    public IncidentAreaResponse[] getIncidentsInArea(IncidentAreaRequest incidentAreaRequest) {
        try {
            String url = basePath + "/api/incident/area";
            return restTemplate.postForObject(url,incidentAreaRequest, IncidentAreaResponse[].class);
        } catch (Exception e) {
            log.error("Got error by get incidents in area request: {}", incidentAreaRequest , e);
            throw e;
        }
    }

    public void updateNotificationDistance(NotificationDistanceUpdateRequest request) {
        try {
            String url = basePath + "/api/settings/notification";
            HttpEntity<NotificationDistanceUpdateRequest> httpEntity = new HttpEntity<>(request);
            restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Void.class);
        } catch (Exception e) {
            log.error("Got error by update notification distance request: {}", request , e);
            throw e;
        }
    }
}
