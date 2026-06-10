package ru.adel.apigateway.public_interface.category.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryDto(
        Long id,
        String code,
        String name,
        String description,
        String baseDangerLevel,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
