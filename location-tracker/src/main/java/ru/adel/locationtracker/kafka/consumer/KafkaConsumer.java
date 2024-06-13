package ru.adel.locationtracker.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.adel.common.kafka.dto.KafkaLocationMessage;
import ru.adel.common.kafka.dto.KafkaUserInteractionMessage;
import ru.adel.common.kafka.dto.enums.KafkaInteractionStatus;
import ru.adel.locationtracker.core.service.notification.db.entity.enums.InteractionStatus;
import ru.adel.locationtracker.public_interface.event.EventService;
import ru.adel.locationtracker.public_interface.event.dto.IncidentUserInteractionDto;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationMessage;

import java.util.UUID;

import static ru.adel.locationtracker.kafka.constant.KafkaConstant.INTERACTION_MESSAGE_CONTAINER;
import static ru.adel.locationtracker.kafka.constant.KafkaConstant.LOCATION_MESSAGE_CONTAINER;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EventService eventService;

    @KafkaListener(
            topics = {"${kafka.consumer.user-location.topic}"},
            containerFactory = LOCATION_MESSAGE_CONTAINER
    )
    public void processUserLocationMessage(ConsumerRecord<UUID, KafkaLocationMessage> message, Acknowledgment ack) {
        log.info("Handle user location message: {}", message.value());

        try {
            eventService.handleUserLocationUpdate(
                    UserLocationMessage.builder()
                            .userId(message.value().userId())
                            .longitude(message.value().longitude())
                            .latitude(message.value().latitude())
                            .timestamp(message.value().timestamp())
                            .build()
            );
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error handling user location message {}: {}", message.value(), e.getMessage());
            throw e;
        }
    }

    @KafkaListener(
            topics = {"${kafka.consumer.user-interaction.topic}"},
            containerFactory = INTERACTION_MESSAGE_CONTAINER
    )
    public void processUserInteractionMessage(ConsumerRecord<UUID, KafkaUserInteractionMessage> message, Acknowledgment ack) {
        log.info("Handle user interaction message: {}", message.value());

        try {
            eventService.handleUserInteractionUpdate(
                    IncidentUserInteractionDto.builder()
                            .userId(message.value().userId())
                            .incidentId(message.value().incidentId())
                            .incidentDate(message.value().incidentDate())
                            .status(mapToInteractionStatus(message.value().status()))
                            .timestamp(message.value().timestamp())
                            .build()
            );
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error handling user interaction message {}: {}", message.value(), e.getMessage());
            throw e;
        }
    }

    private InteractionStatus mapToInteractionStatus(KafkaInteractionStatus kafkaInteractionStatus) {
        return InteractionStatus.valueOf(kafkaInteractionStatus.name());
    }
}
