package com.integrator.application.services.module;

import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.module.TestData;
import com.integrator.application.models.module.TestType;
import com.integrator.application.repositories.module.TestDataRepository;
import com.integrator.application.services.BaseService;
import com.integrator.application.utils.GlobalConstants;
import com.integrator.application.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TestDataService extends BaseService<TestData, Long> {

    private final TestDataRepository testDataRepository;

    @Override
    protected JpaRepository<TestData, Long> getRepository() {
        return testDataRepository;
    }

    @Transactional(readOnly = true)
    public Optional<TestData> getTestDataById(Long id) {
        return testDataRepository.getTestDataById(id);
    }

    @Transactional(readOnly = true)
    public List<TestData> findAllFilter(boolean enabled, String timeZoneId,
                                        String word, String description, TestType testType, Long testTypeId, LocalDate dateStart, LocalDate dateEnd,
                                        Integer limit, Integer offset, Sort sort) {

        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        if (timeZone == null) {
            timeZone = TimeZone.getTimeZone(GlobalConstants.DEFAULT_TIMEZONE);
        }
        return testDataRepository.findAllFilter(
                enabled,
                word,
                description,
                testType,
                testTypeId,
                dateStart != null ? Date.from(dateStart.atStartOfDay().atZone(timeZone.toZoneId()).toInstant()) : null,
                dateEnd != null ? Date.from(dateEnd.atTime(LocalTime.MAX).atZone(timeZone.toZoneId()).toInstant()) : null,
                sort == null ? new OffsetBasedPageRequest(limit, offset) : new OffsetBasedPageRequest(limit, offset, sort)
        );

    }

    @Transactional(readOnly = true)
    public Integer countAllFilter(boolean enabled, String timeZoneId,
                                  String word, String description, TestType testType, LocalDate dateStart, LocalDate dateEnd) {

        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        if (timeZone == null) {
            timeZone = TimeZone.getTimeZone(GlobalConstants.DEFAULT_TIMEZONE);
        }

        return testDataRepository.countAllFilter(
                enabled,
                word,
                description,
                testType,
                dateStart != null ? Date.from(dateStart.atStartOfDay().atZone(timeZone.toZoneId()).toInstant()) : null,
                dateEnd != null ? Date.from(dateEnd.atTime(LocalTime.MAX).atZone(timeZone.toZoneId()).toInstant()) : null
        );
    }

    public TestData saveTestData(TestData testData, UserSetting userSetting) {
        if (testData == null) {
            throw new ResourceNotFoundException("Value can't be null");
        }
        testData = saveAndFlush(testData);

        return testData;
    }
}
