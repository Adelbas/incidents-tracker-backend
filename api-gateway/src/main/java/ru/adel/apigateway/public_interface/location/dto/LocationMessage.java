package ru.adel.apigateway.public_interface.location.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LocationMessage (
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
){ }
