package ru.adel.apigateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.apigateway.public_interface.incident.IncidentService;
import ru.adel.apigateway.public_interface.incident.dto.IncidentUserInteractionMessage;
import ru.adel.apigateway.public_interface.location.LocationService;
import ru.adel.apigateway.public_interface.location.dto.LocationMessage;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final LocationService locationService;

    private final IncidentService incidentService;

    @MessageMapping("/location")
    public void handleLocationMessage(Principal principal, @Payload LocationMessage locationMessage) {
        log.info("Handle user location message: {}", locationMessage);
        UUID userId = UUID.fromString(principal.getName());

        locationService.saveLocation(userId, locationMessage);
    }

    @MessageMapping("/interaction")
    public void handleInteractionMessage(Principal principal, @Payload IncidentUserInteractionMessage interactionMessage) {
        log.info("Handle user interaction message: {}", interactionMessage);
        UUID userId = UUID.fromString(principal.getName());

        incidentService.handleUserInteraction(userId, interactionMessage);
    }
}
