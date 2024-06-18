package ru.adel.locationtracker.core.service.location.incident.db;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.location.utils.GeoSpatialService;
import ru.adel.locationtracker.core.service.location.incident.db.entity.Incident;
import ru.adel.locationtracker.public_interface.exception.IncidentNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentDbService {

    private final IncidentRepository incidentRepository;

    private final GeoSpatialService geoSpatialService;

    public Incident save(Incident incident) {
        incident.setCoordinates(
                geoSpatialService.getPoint(
                        incident.getLongitude(),
                        incident.getLatitude()
                )
        );
        return incidentRepository.save(incident);
    }

    public Incident getByIdAndDate(Long id, LocalDate date) {
        return incidentRepository.findByCreatedAtDateAndId(date, id)
                .orElseThrow(() -> new IncidentNotFoundException("Incident not found with id " + id));
    }

    public List<IncidentDto> getIncidentsNearbyForUser(UUID userId, Double longitude, Double latitude, Integer distance) {
        Point point = geoSpatialService.getPoint(longitude, latitude);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        return incidentRepository.findAllIncidentsNearbyUserCoordinateInDistance(
                userId,
                point,
                distance,
                today,
                yesterday
        );
    }

    public List<IncidentDto> getIncidentsInAreaForUser(
            Double longitudeMin, Double latitudeMin,
            Double longitudeMax, Double latitudeMax,
            LocalDate startDate, LocalDate endDate) {
        int srid = geoSpatialService.getSrid();
        return incidentRepository.findAllIncidentsInAreaForUserByCreatedAtBetween(
                longitudeMin,
                latitudeMin,
                longitudeMax,
                latitudeMax,
                startDate,
                endDate,
                srid
        );
    }
}
