package ru.adel.locationtracker.core.service.location.incident.db;

import java.time.LocalDateTime;

/**
 * Spring Data projection. Getter names must match the column aliases of the native queries.
 */
public interface IncidentDto {

    Long getId();

    String getTitle();

    String getCategoryCode();

    String getCategoryName();

    String getDangerLevel();

    Double getLongitude();

    Double getLatitude();

    LocalDateTime getCreatedAt();
}
