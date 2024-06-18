package ru.adel.apigateway.kafka.configuration;

import lombok.Setter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import ru.adel.apigateway.kafka.configuration.property.KafkaTopicProperty;

import java.util.HashMap;
import java.util.Map;

@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaTopicConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddresses;

    private KafkaTopicProperty userLocation;

    private KafkaTopicProperty userInteraction;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddresses);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userLocationTopic() {
        return TopicBuilder.name(userLocation.topic())
                .partitions(userLocation.partitions())
                .replicas(userLocation.replicas())
                .build();
    }

    @Bean
    public NewTopic userInteractionTopic() {
        return TopicBuilder.name(userInteraction.topic())
                .partitions(userInteraction.partitions())
                .replicas(userInteraction.replicas())
                .build();
    }
}
