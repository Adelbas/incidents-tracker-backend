//package ru.adel.apigateway.kafka.configuration;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.UUIDDeserializer;
//import org.apache.kafka.common.serialization.UUIDSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import ru.adel.common.kafka.dto.KafkaNotificationMessage;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@EnableKafka
//@Configuration
//public class KafkaConfiguration {
//
//    @Value(value = "${spring.kafka.bootstrap-servers}")
//    private String bootstrapAddress;
//
//    @Value("${kafka.consumer.group-id}")
//    private String consumerGroupId;
//
//    @Bean
//    public ConsumerFactory<UUID, KafkaNotificationMessage> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        return new DefaultKafkaConsumerFactory<>(props, new UUIDDeserializer(),
//                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(KafkaNotificationMessage.class)));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<UUID, KafkaNotificationMessage> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<UUID, KafkaNotificationMessage> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }
//
//    @Bean
//    public ProducerFactory<UUID, Object> producerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        props.put(ProducerConfig.ACKS_CONFIG, "1");
//        return new DefaultKafkaProducerFactory<>(props);
//    }
//
//    @Bean
//    public KafkaTemplate<UUID, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//}
//
