package ru.adel.apigateway.core.service.incident;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.incident.client.IncidentClientService;
import ru.adel.apigateway.kafka.producer.KafkaInteractionProducer;
import ru.adel.apigateway.public_interface.incident.IncidentService;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentAreaResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentGetResponse;
import ru.adel.apigateway.public_interface.incident.dto.IncidentPostRequest;
import ru.adel.apigateway.public_interface.incident.dto.IncidentUserInteractionMessage;
import ru.adel.common.kafka.dto.KafkaUserInteractionMessage;
import ru.adel.common.kafka.dto.enums.KafkaInteractionStatus;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final KafkaInteractionProducer kafkaInteractionProducer;

    private final IncidentClientService incidentClientService;

    @Override
    public IncidentGetResponse getIncident(Long id) {
        return incidentClientService.getIncident(id);
    }

    @Override
    public void createIncident(UUID userId, IncidentPostRequest incidentPostRequest) {
        incidentClientService.createIncident(userId, incidentPostRequest);
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
        return incidentClientService.getIncidentsInArea(incidentAreaRequest);
    }
}
