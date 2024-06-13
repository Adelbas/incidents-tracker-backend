//package ru.adel.apigateway.kafka.configuration;
//
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.KafkaAdmin;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaTopicConfiguration {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapAddresses;
//
//    @Value("${kafka.producer.user-location.topic}")
//    private String userLocationTopic;
//
//    @Value("${kafka.producer.user-interaction.topic}")
//    private String userInteractionTopic;
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddresses);
//        return new KafkaAdmin(configs);
//    }
//
//    @Bean
//    public NewTopic userLocationTopic() {
//        return TopicBuilder.name(userLocationTopic)
//                .partitions(2)
//                .replicas(2)
//                .build();
//    }
//
//    @Bean
//    public NewTopic userInteractionTopic() {
//        return TopicBuilder.name(userInteractionTopic)
//                .partitions(2)
//                .replicas(2)
//                .build();
//    }
//}
