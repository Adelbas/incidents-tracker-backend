package ru.adel.apigateway.public_interface.category.dto;

import lombok.Builder;

@Builder
public record CategoryUpsertRequest(
        String code,
        String name,
        String description,
        String baseDangerLevel,
        Boolean active
) { }
