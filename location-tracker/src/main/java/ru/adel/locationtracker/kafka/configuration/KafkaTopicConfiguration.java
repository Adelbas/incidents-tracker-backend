package ru.adel.locationtracker.kafka.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddresses;

    @Value("${kafka.producer.notification-message.topic}")
    private String notificationMessageTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddresses);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic notificationMessageTopic() {
        return TopicBuilder.name(notificationMessageTopic)
                .partitions(2)
                .replicas(2)
                .build();
    }
}
