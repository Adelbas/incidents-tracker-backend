package ru.adel.apigateway.core.service.incident;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.authentication.db.user.UserDbService;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;
import ru.adel.apigateway.core.service.incident.client.IncidentClientService;
import ru.adel.apigateway.core.service.incident.client.dto.IncidentResponse;
import ru.adel.apigateway.kafka.producer.KafkaInteractionProducer;
import ru.adel.apigateway.public_interface.incident.IncidentService;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentGetResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentPostRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentUserInteractionMessage;
import ru.adel.common.kafka.dto.KafkaUserInteractionMessage;
import ru.adel.common.kafka.dto.enums.KafkaInteractionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final KafkaInteractionProducer kafkaInteractionProducer;

    private final IncidentClientService incidentClientService;

    private final UserDbService userDbService;

    @Override
    public IncidentGetResponse getIncident(Long id, LocalDate date) {
        IncidentResponse response = incidentClientService.getIncident(id, date);
        User user = userDbService.getById(response.postedUserId());
        return IncidentGetResponse.builder()
                .id(response.id())
                .title(response.title())
                .longitude(response.longitude())
                .latitude(response.latitude())
                .postedUserId(user.getId())
                .postedUserLastName(user.getLastName())
                .postedUserFirstName(user.getFirstName())
                .image(response.image())
                .views(response.views())
                .createdAt(response.createdAt())
                .build();
    }

    @Override
    public void createIncident(String username, IncidentPostRequest incidentPostRequest) {
        User user = userDbService.getByEmail(username);
        incidentClientService.createIncident(user.getId(), incidentPostRequest);
    }

    @Override
    public void handleUserInteraction(UUID userId, IncidentUserInteractionMessage interactionMessage) {
        kafkaInteractionProducer.send(
                KafkaUserInteractionMessage.builder()
                        .userId(userId)
                        .incidentId(interactionMessage.incidentId())
                        .incidentDate(interactionMessage.incidentDate())
                        .status(KafkaInteractionStatus.valueOf(interactionMessage.status().name()))
                        .timestamp(interactionMessage.timestamp())
                        .build()
        );
    }

    @Override
    public List<IncidentAreaResponse> getIncidentsInArea(IncidentAreaRequest incidentAreaRequest) {
        List<IncidentAreaResponse> incidents = incidentClientService.getIncidentsInArea(incidentAreaRequest);
        log.info("Get incidents area response: {}", incidents);
        return incidents;
    }

    @Override
    public void updateNotificationDistance(String username, int distance) {
        User user = userDbService.getByEmail(username);
        incidentClientService.updateNotificationDistance(user.getId(), distance);
    }
}
