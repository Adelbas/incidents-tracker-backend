package ru.adel.apigateway.kafka.configuration.property;

public record KafkaTopicProperty(
    String topic,
    int partitions,
    int replicas
) { }
