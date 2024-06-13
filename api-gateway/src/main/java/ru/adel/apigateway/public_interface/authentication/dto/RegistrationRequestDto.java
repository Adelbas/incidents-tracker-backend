package ru.adel.apigateway.public_interface.authentication.dto;

import lombok.Builder;

@Builder
public record RegistrationRequestDto(
        String firstname,
        String lastname,
        String email,
        String password
) { }
