package ru.adel.locationtracker.public_interface.rest;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IncidentPostRequest(
        UUID postedUserId,
        String title,
        Double latitude,
        Double longitude
) { }
