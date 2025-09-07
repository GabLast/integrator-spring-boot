package com.integrator.application.services.module;

import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.module.TestData;
import com.integrator.application.models.module.TestType;
import com.integrator.application.repositories.module.TestDataRepository;
import com.integrator.application.services.BaseService;
import com.integrator.application.utils.DateUtilities;
import com.integrator.application.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TestDataService extends BaseService<TestData, Long> {

    private final TestDataRepository testDataRepository;
    private final Executor asyncExecutorBean;

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
                                        String word, String description, TestType testType, Long testTypeId,
                                        LocalDate dateStart, LocalDate dateEnd,
                                        Integer limit, Integer offset, Sort sort) {
        return testDataRepository.findAllFilter(
                enabled,
                word,
                description,
                testType,
                testTypeId,
                DateUtilities.getLocalDateTimeAtTimeZoneAtStartOrEnd(timeZoneId, dateStart, true),
                DateUtilities.getLocalDateTimeAtTimeZoneAtStartOrEnd(timeZoneId, dateEnd, false),
                sort == null ? new OffsetBasedPageRequest(limit, offset) : new OffsetBasedPageRequest(limit, offset, sort)
        );

    }

    @Transactional(readOnly = true)
    public Integer countAllFilter(boolean enabled, String timeZoneId,
                                  String word, String description, TestType testType, LocalDate dateStart, LocalDate dateEnd) {
        return testDataRepository.countAllFilter(
                enabled,
                word,
                description,
                testType,
                DateUtilities.getLocalDateTimeAtTimeZoneAtStartOrEnd(timeZoneId, dateStart, true),
                DateUtilities.getLocalDateTimeAtTimeZoneAtStartOrEnd(timeZoneId, dateEnd, false)
        );
    }

    public TestData saveTestData(TestData testData) throws InterruptedException {
        if (testData == null) {
            throw new ResourceNotFoundException("Value can't be null");
        }
        testData = saveAndFlush(testData);

        //beware, this runs once on start up, before even calling this service method. why?
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10 * 1000); //simulate fetching
                asyncExecutorBean.execute(this::simpleExecutorTest);
                return true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        future.thenAccept(value -> {
            if (value) {
                log.info("Future Completed");
            } else {
                log.info("Future Failed");
            }
        });

        log.info("Continued to return");

        return testData;
    }

    private void simpleExecutorTest() {
        log.info("Executor Task Executed through an Executor");
    }
}
