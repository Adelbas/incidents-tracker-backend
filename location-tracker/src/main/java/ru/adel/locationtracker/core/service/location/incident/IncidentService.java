package ru.adel.locationtracker.core.service.location.incident;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.location.incident.db.IncidentDbService;
import ru.adel.locationtracker.core.service.location.incident.db.IncidentDto;
import ru.adel.locationtracker.core.service.location.incident.db.entity.Incident;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaRequest;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentGetResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentPostRequest;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentDbService incidentDbService;

    public IncidentGetResponse getIncident(Long id) {
        Incident incident = incidentDbService.getById(id);
        return mapToGetResponse(incident);
    }

    public IncidentNotificationDto createIncident(IncidentPostRequest incidentPostRequest) {
        Incident incident = incidentDbService.save(
                Incident.builder()
                        .postedUserId(incidentPostRequest.postedUserId())
                        .title(incidentPostRequest.title())
                        .latitude(incidentPostRequest.latitude())
                        .longitude(incidentPostRequest.longitude())
                        .views(0)
                        .createdAt(LocalDateTime.now())
                        .createdAtDate(LocalDate.now())
                        .build()
        );

        return mapToNotificationDto(incident);
    }

    public List<IncidentNotificationDto> getIncidentsNearbyUserLocation(UserLocationDto userLocationDto) {
        return incidentDbService.getIncidentsNearbyForUser(
                userLocationDto.userId(),
                userLocationDto.longitude(),
                userLocationDto.latitude(),
                userLocationDto.notificationDistance()
        ).stream()
                .map(this::mapToNotificationDto)
                .toList();
    }

    public void incrementViews(Long incidentId) {
        Incident incident = incidentDbService.getById(incidentId);
        incident.setViews(incident.getViews() + 1);
        incidentDbService.save(incident);
    }

    public List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest) {
        return incidentDbService.getIncidentsInAreaForUser(
                incidentAreaRequest.longitudeMin(),
                incidentAreaRequest.latitudeMin(),
                incidentAreaRequest.longitudeMax(),
                incidentAreaRequest.latitudeMax(),
                incidentAreaRequest.startDate(),
                incidentAreaRequest.endDate()
        ).stream()
                .map(this::mapToAreResponse)
                .toList();
    }

    private IncidentGetResponse mapToGetResponse(Incident incident) {
        return IncidentGetResponse.builder()
                .id(incident.getId())
                .postedUserId(incident.getPostedUserId())
                .title(incident.getTitle())
                .latitude(incident.getLatitude())
                .longitude(incident.getLongitude())
                .views(incident.getViews())
                .createdAt(incident.getCreatedAt())
                .build();
    }

    private IncidentNotificationDto mapToNotificationDto(Incident incident) {
        return IncidentNotificationDto.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .longitude(incident.getLongitude())
                .latitude(incident.getLatitude())
                .createdAt(incident.getCreatedAt())
                .build();
    }

    private IncidentNotificationDto mapToNotificationDto(IncidentDto incidentDto) {
        return IncidentNotificationDto.builder()
                .id(incidentDto.getId())
                .title(incidentDto.getTitle())
                .longitude(incidentDto.getLongitude())
                .latitude(incidentDto.getLatitude())
                .createdAt(incidentDto.getCreatedAt())
                .build();
    }

    private IncidentAreaResponse mapToAreResponse(IncidentDto incidentDto) {
        return IncidentAreaResponse.builder()
                .id(incidentDto.getId())
                .title(incidentDto.getTitle())
                .longitude(incidentDto.getLongitude())
                .latitude(incidentDto.getLatitude())
                .createdAt(incidentDto.getCreatedAt())
                .build();
    }
}
