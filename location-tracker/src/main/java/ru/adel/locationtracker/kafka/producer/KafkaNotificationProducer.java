package ru.adel.locationtracker.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.adel.common.kafka.dto.KafkaNotificationMessage;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    private final KafkaTemplate<UUID, KafkaNotificationMessage> kafkaTemplate;

    @Value("${kafka.producer.notification-message.topic}")
    private String notificationMessageTopic;

    public void send(KafkaNotificationMessage message) {
        log.info("Sending notification message to kafka: {}", message);
        try {
            kafkaTemplate.send(notificationMessageTopic, message.userId(), message)
                    .exceptionally(exception -> {
                        processSendingError(message, exception);
                        return null;
                    });
        } catch (Exception e) {
            processSendingError(message, e);
        }
    }

    private void processSendingError(KafkaNotificationMessage message, Throwable t) {
        //Simple error processing
        log.error("Error producing notification message {}: {}", message, t.getMessage());
    }
}
