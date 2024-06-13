package ru.adel.apigateway.public_interface.incident.dto;

import java.time.LocalDate;

public record IncidentAreaRequest(
        Double longitudeMin,
        Double latitudeMin,
        Double longitudeMax,
        Double latitudeMax,
        LocalDate startDate,
        LocalDate endDate
) { }
