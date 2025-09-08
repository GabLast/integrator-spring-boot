package com.integrator.application.kafka.consumer;

import com.integrator.application.config.kafka.KafkaTopicConfig;
import com.integrator.application.kafka.dto.TestDataKafka;
import com.integrator.application.kafka.dto.UserKafka;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(
        id = "complex-consumer",
        topics = KafkaTopicConfig.COMPLEX_TOPIC,
        containerFactory = "complexListenerFactory"
)
public class ComplexKafkaConsumer {

    @KafkaHandler
    public void handleTestData(TestDataKafka payload) {
        System.out.println("ComplexKafkaConsumer handleTestData: " + payload.getWord());
    }

    @KafkaHandler
    public void handleUser(UserKafka payload) {
        System.out.println("ComplexKafkaConsumer handleUser: " + payload.getUsername());
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object object) {
        System.out.println("ComplexKafkaConsumer Unknown Type: " + object);
    }

}
