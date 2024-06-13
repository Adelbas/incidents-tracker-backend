package ru.adel.locationtracker.core.service.location.incident.db;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.adel.locationtracker.core.service.location.incident.db.entity.Incident;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    @Query(
            value = "SELECT i.id, i.title, i.longitude, i.latitude, i.created_at AS createdAt " +
                    "FROM incidents i " +
                    "WHERE (created_at_date = :today OR  created_at_date = :yesterday) " +
                    "   AND st_dwithin(" +
                    "               i.coordinates," +
                    "               :point," +
                    "               :distance " +
                    "      )" +
                    "   AND NOT EXISTS (SELECT 1 " +
                    "                   FROM incident_user_interaction iui " +
                    "                   WHERE (iui.incident_date = :today OR iui.incident_date = :yesterday) " +
                    "                   AND iui.incident_id = i.id " +
                    "                   AND iui.user_id = :userId)",
            nativeQuery = true
    )
    List<IncidentDto> findAllIncidentsNearbyUserCoordinateInDistance(UUID userId, Point point, Integer distance, LocalDate today, LocalDate yesterday);

    @Query(
            value = "SELECT i.id, i.title, i.longitude, i.latitude, i.created_at AS createdAt " +
                    "FROM incidents i " +
                    "WHERE (created_at_date BETWEEN :startDate AND :endDate) " +
                    "   AND st_intersects(" +
                    "               i.coordinates," +
                    "               ST_MakeEnvelope(:longitudeMin, :latitudeMin, :longitudeMax, :latitudeMax, :srid) " +
                    "      )",
            nativeQuery = true
    )
    List<IncidentDto> findAllIncidentsInAreaForUserByCreatedAtBetween(
            Double longitudeMin, Double latitudeMin,
            Double longitudeMax, Double latitudeMax,
            LocalDate startDate, LocalDate endDate,
            int srid
    );
}
