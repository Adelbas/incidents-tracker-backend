package ru.adel.apigateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.apigateway.public_interface.incident.IncidentService;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final IncidentService incidentService;

    @PutMapping("/notification")
    public void updateNotificationDistance(Principal principal, @RequestParam int distance) {
        log.info("Handle update notification distance to <{}> request for user {}", distance, principal.getName());
        incidentService.updateNotificationDistance(principal.getName(), distance);
    }
}
