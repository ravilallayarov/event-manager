package org.example.eventnotificator.kafka.config;

import org.example.eventnotificator.notification.dto.EventKafkaMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<Long, EventKafkaMessage> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        var factory = new DefaultKafkaConsumerFactory<Long, EventKafkaMessage>(props);
        factory.setValueDeserializer(new JsonDeserializer<>(EventKafkaMessage.class, false));

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, EventKafkaMessage> factory(
            ConsumerFactory<Long, EventKafkaMessage> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, EventKafkaMessage>();
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
