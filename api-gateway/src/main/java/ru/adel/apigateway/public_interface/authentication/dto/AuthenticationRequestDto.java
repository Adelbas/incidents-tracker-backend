package ru.adel.apigateway.public_interface.authentication.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequestDto(
        String email,
        String password
) { }
