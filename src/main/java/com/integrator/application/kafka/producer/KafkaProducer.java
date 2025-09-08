package com.integrator.application.kafka.producer;

import com.integrator.application.kafka.dto.TestDataKafka;
import com.integrator.application.kafka.dto.UserKafka;
import com.integrator.application.models.module.TestData;
import com.integrator.application.models.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.integrator.application.config.kafka.KafkaTopicConfig.COMPLEX_TOPIC;
import static com.integrator.application.config.kafka.KafkaTopicConfig.SIMPLE_TOPIC;
import static com.integrator.application.config.kafka.KafkaTopicConfig.TEST_DATA_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final ModelMapper modelMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, TestData> testDataKafkaTemplate;
    private final KafkaTemplate<String, Object> complexKafkaTemplate;

    public void sendData(String data) {
        kafkaTemplate.send(SIMPLE_TOPIC, "simple-consumer", data);
    }

    public void sendTestData(TestData data) {
        testDataKafkaTemplate.send(TEST_DATA_TOPIC, "test-data-consumer", data);
    }

    public void complexSend(TestData data, User user) {
        complexKafkaTemplate.send(COMPLEX_TOPIC, modelMapper.map(user, UserKafka.class));
        complexKafkaTemplate.send(COMPLEX_TOPIC, "complex-consumer", modelMapper.map(data, TestDataKafka.class));
    }
}
