package ru.adel.apigateway.core.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.kafka.producer.KafkaLocationProducer;
import ru.adel.apigateway.public_interface.location.LocationService;
import ru.adel.apigateway.public_interface.location.dto.LocationMessage;
import ru.adel.common.kafka.dto.KafkaLocationMessage;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final KafkaLocationProducer kafkaLocationProducer;

    @Override
    public void saveLocation(UUID userId, LocationMessage locationMessage) {
        kafkaLocationProducer.send(
                KafkaLocationMessage.builder()
                        .userId(userId)
                        .latitude(locationMessage.latitude())
                        .longitude(locationMessage.longitude())
                        .timestamp(locationMessage.timestamp())
                        .build()
        );
    }
}
