package com.integrator.application.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String SIMPLE_TOPIC = "simple_topic";
    public static final String TEST_DATA_TOPIC = "test_data_topic";
    public static final String COMPLEX_TOPIC = "complex_topic";

    //    create topics programmatically.
    @Bean
    public NewTopic simpleTopic() {
        return TopicBuilder.name(SIMPLE_TOPIC)
                .build();
    }
    @Bean
    public NewTopic testDataTopic() {
        return TopicBuilder.name(TEST_DATA_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic complexTopic() {
        return TopicBuilder.name(COMPLEX_TOPIC)
                .build();
    }
    @Bean
    public KafkaErrorHandler kafkaErrorHandler() {
        return new KafkaErrorHandler();
    }

}
