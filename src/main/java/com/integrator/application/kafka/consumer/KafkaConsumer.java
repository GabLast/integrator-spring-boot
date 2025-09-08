package com.integrator.application.kafka.consumer;

import com.integrator.application.config.kafka.KafkaTopicConfig;
import com.integrator.application.models.module.TestData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(
            topics = KafkaTopicConfig.SIMPLE_TOPIC,
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "simple-consumer"
    )
    public void simpleListener(@Payload String message,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        System.out.printf("Received message: %s from partition: %d\n", message, partition);
    }

    @KafkaListener(
            topics = KafkaTopicConfig.TEST_DATA_TOPIC,
            containerFactory = "testDataListenerFactory",
            groupId = "test-data-consumer"
    )
    public void testDataListener(@Payload TestData payload) {
        System.out.printf("Received data from testDataListener: %d %s %tm\n", payload.getId(), payload.getWord(), payload.getDate());
    }

}
