package ru.adel.locationtracker.public_interface.rest;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IncidentPostRequest(
        UUID postedUserId,
        String title,
        String description,
        Double latitude,
        Double longitude,
        byte[] image
) { }
