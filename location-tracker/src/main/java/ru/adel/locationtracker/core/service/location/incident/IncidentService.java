package ru.adel.locationtracker.core.service.location.incident;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.analysis.CategorizationResult;
import ru.adel.locationtracker.core.service.analysis.CategorizationService;
import ru.adel.locationtracker.core.service.analysis.category.CategoryService;
import ru.adel.locationtracker.core.service.analysis.category.db.entity.Category;
import ru.adel.locationtracker.core.service.location.incident.db.IncidentDbService;
import ru.adel.locationtracker.core.service.location.incident.db.IncidentDto;
import ru.adel.locationtracker.core.service.location.incident.db.entity.Incident;
import ru.adel.locationtracker.public_interface.analysis.DangerLevel;
import ru.adel.locationtracker.public_interface.event.dto.IncidentNotificationDto;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaRequest;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentGetResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentPostRequest;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentDbService incidentDbService;

    private final CategorizationService categorizationService;

    private final CategoryService categoryService;

    public IncidentGetResponse getIncident(Long id, LocalDate date) {
        Incident incident = incidentDbService.getByIdAndDate(id, date);
        return mapToGetResponse(incident);
    }

    public IncidentNotificationDto createIncident(IncidentPostRequest incidentPostRequest) {
        CategorizationResult categorization = categorizationService.categorize(
                incidentPostRequest.title(),
                incidentPostRequest.description()
        );

        Incident incident = incidentDbService.save(
                Incident.builder()
                        .postedUserId(incidentPostRequest.postedUserId())
                        .title(incidentPostRequest.title())
                        .description(incidentPostRequest.description())
                        .categoryId(categorization.categoryId())
                        .dangerLevel(categorization.dangerLevel())
                        .latitude(incidentPostRequest.latitude())
                        .longitude(incidentPostRequest.longitude())
                        .views(1)
                        .image(incidentPostRequest.image())
                        .createdAt(LocalDateTime.now())
                        .createdAtDate(LocalDate.now())
                        .build()
        );

        return IncidentNotificationDto.builder()
                .id(incident.getId())
                .title(incident.getTitle())
                .categoryCode(categorization.categoryCode())
                .categoryName(categorization.categoryName())
                .dangerLevel(categorization.dangerLevel())
                .longitude(incident.getLongitude())
                .latitude(incident.getLatitude())
                .createdAt(incident.getCreatedAt())
                .build();
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

    public void incrementViews(Long incidentId, LocalDate incidentDate) {
        Incident incident = incidentDbService.getByIdAndDate(incidentId, incidentDate);
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
        Optional<Category> category = incident.getCategoryId() == null
                ? Optional.empty()
                : categoryService.findById(incident.getCategoryId());

        return IncidentGetResponse.builder()
                .id(incident.getId())
                .postedUserId(incident.getPostedUserId())
                .title(incident.getTitle())
                .description(incident.getDescription())
                .categoryCode(category.map(Category::getCode).orElse(null))
                .categoryName(category.map(Category::getName).orElse(null))
                .dangerLevel(incident.getDangerLevel())
                .latitude(incident.getLatitude())
                .longitude(incident.getLongitude())
                .views(incident.getViews())
                .image(incident.getImage())
                .createdAt(incident.getCreatedAt())
                .build();
    }

    private IncidentNotificationDto mapToNotificationDto(IncidentDto incidentDto) {
        return IncidentNotificationDto.builder()
                .id(incidentDto.getId())
                .title(incidentDto.getTitle())
                .categoryCode(incidentDto.getCategoryCode())
                .categoryName(incidentDto.getCategoryName())
                .dangerLevel(parseDangerLevel(incidentDto.getDangerLevel()))
                .longitude(incidentDto.getLongitude())
                .latitude(incidentDto.getLatitude())
                .createdAt(incidentDto.getCreatedAt())
                .build();
    }

    private IncidentAreaResponse mapToAreResponse(IncidentDto incidentDto) {
        return IncidentAreaResponse.builder()
                .id(incidentDto.getId())
                .title(incidentDto.getTitle())
                .categoryCode(incidentDto.getCategoryCode())
                .categoryName(incidentDto.getCategoryName())
                .dangerLevel(parseDangerLevel(incidentDto.getDangerLevel()))
                .longitude(incidentDto.getLongitude())
                .latitude(incidentDto.getLatitude())
                .createdAt(incidentDto.getCreatedAt())
                .build();
    }

    private static DangerLevel parseDangerLevel(String value) {
        return value == null ? null : DangerLevel.valueOf(value);
    }
}
