package ru.adel.apigateway.core.ws;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.UUID;

@RequiredArgsConstructor
public class StompPrincipal implements Principal {

    private final UUID id;

    @Override
    public String getName() {
        return id.toString();
    }
}