package ru.adel.locationtracker.public_interface.event.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record NotificationDistanceUpdateRequest(
        UUID userId,
        int distance
) { }