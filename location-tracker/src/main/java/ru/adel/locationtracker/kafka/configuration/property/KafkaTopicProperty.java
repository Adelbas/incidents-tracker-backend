package ru.adel.locationtracker.kafka.configuration.property;

public record KafkaTopicProperty(
        String topic,
        int partitions,
        int replicas
) { }
