package ru.adel.locationtracker.core.service.location.incident.db;

import java.time.LocalDateTime;

public interface IncidentDto {

    Long getId();

    String getTitle();

    Double getLongitude();

    Double getLatitude();

    LocalDateTime getCreatedAt();
}
