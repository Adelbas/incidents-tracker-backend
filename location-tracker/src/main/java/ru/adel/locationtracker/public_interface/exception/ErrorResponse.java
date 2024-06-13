package ru.adel.locationtracker.public_interface.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String path
) {
}
