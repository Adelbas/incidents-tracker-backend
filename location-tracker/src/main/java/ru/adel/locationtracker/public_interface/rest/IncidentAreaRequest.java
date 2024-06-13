package ru.adel.locationtracker.public_interface.rest;

import java.time.LocalDate;

public record IncidentAreaRequest(
        Double longitudeMin,
        Double latitudeMin,
        Double longitudeMax,
        Double latitudeMax,
        LocalDate startDate,
        LocalDate endDate
) { }
