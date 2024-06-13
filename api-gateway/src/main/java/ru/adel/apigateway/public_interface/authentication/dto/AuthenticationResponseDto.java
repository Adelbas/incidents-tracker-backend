package ru.adel.apigateway.public_interface.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken
) { }
