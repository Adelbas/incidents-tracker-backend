package ru.adel.apigateway.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.public_interface.notification.NotificationService;
import ru.adel.apigateway.public_interface.notification.dto.NotificationMessage;
import ru.adel.common.kafka.dto.KafkaNotificationMessage;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = {"${kafka.consumer.notification-message.topic}"})
    public void processNotificationMessage(ConsumerRecord<UUID, KafkaNotificationMessage> message, Acknowledgment ack) {
        log.info("Handle notification message for user {}: {}", message.key(), message.value());

        try {
            notificationService.sendNotification(
                    message.value().userId(),
                    NotificationMessage.builder()
                            .title(message.value().title())
                            .incidentId(message.value().incidentId())
                            .longitude(message.value().longitude())
                            .latitude(message.value().latitude())
                            .timestamp(message.value().timestamp())
                            .build()
            );
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error handling notification message {}: {}", message.value(), e.getMessage());
            throw e;
        }
    }
}
