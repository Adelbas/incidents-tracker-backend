package ru.adel.apigateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.apigateway.core.service.authentication.security.details.UserDetailsImpl;
import ru.adel.apigateway.public_interface.incident.IncidentService;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentGetResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentPostRequest;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/incident")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    private final UserDetailsService userDetailsService;

    @GetMapping("/{id}")
    public IncidentGetResponse getIncident(@PathVariable Long id) {
        log.info("Handle get incident <{}> request", id);
        return incidentService.getIncident(id);
    }

    @PostMapping
    public void createIncident(Principal principal, @RequestBody IncidentPostRequest incidentPostRequest) {
        log.info("Handle create incident request: {}", incidentPostRequest);
        UUID userId = ((UserDetailsImpl) userDetailsService.loadUserByUsername(principal.getName())).getId();

        incidentService.createIncident(userId, incidentPostRequest);
    }

    @PostMapping("/area")
    public List<IncidentAreaResponse> getIncidentsInArea(@RequestBody IncidentAreaRequest incidentAreaRequest) {
        log.info("Handle get incidents in area request: {}", incidentAreaRequest);

        return incidentService.getIncidentsInArea(incidentAreaRequest);
    }
}
