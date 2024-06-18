package ru.adel.apigateway.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.adel.common.kafka.dto.KafkaUserInteractionMessage;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaInteractionProducer {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    @Value("${kafka.producer.user-interaction.topic}")
    private String userInteractionTopic;

    public void send(KafkaUserInteractionMessage message) {
        log.info("Sending location message to kafka {}", message);
        try {
            kafkaTemplate.send(userInteractionTopic, message.userId(), message)
                    .exceptionally(exception -> {
                        processSendingError(message, exception);
                        return null;
                    });
        } catch (Exception e) {
            processSendingError(message, e);
        }
    }

    private void processSendingError(KafkaUserInteractionMessage message, Throwable t) {
        //Simple error processing
        log.error("Error producing user interaction message {}: {}", message, t.getMessage());
    }
}
