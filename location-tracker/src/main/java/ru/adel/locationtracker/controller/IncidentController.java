package ru.adel.locationtracker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.locationtracker.public_interface.event.EventService;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaRequest;
import ru.adel.locationtracker.public_interface.rest.IncidentAreaResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentGetResponse;
import ru.adel.locationtracker.public_interface.rest.IncidentPostRequest;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/incident")
@RequiredArgsConstructor
public class IncidentController {

    private final EventService eventService;

    @PostMapping
    public void createIncident(@RequestBody IncidentPostRequest incidentPostRequest) {
        log.info("Handle create incident request, image size = {}, : {}", incidentPostRequest.image().length, incidentPostRequest);
        eventService.createIncident(incidentPostRequest);
    }

    @GetMapping("/{id}")
    public IncidentGetResponse getIncident(@PathVariable Long id, @RequestParam LocalDate date) {
        log.info("Handle get incident request by id <{}> date <{}>", id, date);
        return eventService.getIncident(id, date);
    }

    @PostMapping("/area")
    public List<IncidentAreaResponse> getIncidentsInArea(@RequestBody IncidentAreaRequest incidentAreaRequest) {
        log.info("Handle get incidents in area request: {}", incidentAreaRequest);
        return eventService.getIncidentsInArea(incidentAreaRequest);
    }
}
