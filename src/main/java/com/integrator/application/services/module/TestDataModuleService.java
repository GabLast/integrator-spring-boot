package com.integrator.application.services.module;

import com.integrator.application.dto.request.module.TestDataDto;
import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.kafka.producer.KafkaProducer;
import com.integrator.application.models.module.TestData;
import com.integrator.application.models.security.User;
import com.integrator.application.utils.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TestDataModuleService {

    private final TestDataService testDataService;
    private final TestTypeService testTypeService;
    private final KafkaProducer kafkaProducer;

    public TestData saveTestData(TestDataDto value) {
        if (value == null) {
            throw new ResourceNotFoundException("Value can't be null");
        }

        TestData build;

        if (value.id() == null || value.id() == 0L) {
            build = TestData.builder()
                    .word(value.word())
                    .testType(testTypeService.get(value.testTypeId()).orElse(null))
                    .description(value.description())
                    .date(value.date())
                    .number(value.number())
                    .build();
        } else {
            build = testDataService.getTestDataById(value.id()).orElseThrow();
            build.setWord(value.word());
            build.setTestType(testTypeService.get(value.testTypeId()).orElse(null));
            build.setDescription(value.description());
            build.setNumber(value.number());
            build.setDate(value.date());
        }

        build.setDate(DateUtilities.getLocalDateTimeAtTimeZone(null, build.getDate()));

        build = testDataService.saveTestData(build);
        kafkaProducer.sendData("simple");
        kafkaProducer.sendTestData(build);
        kafkaProducer.complexSend(build, (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return build;
    }
}
