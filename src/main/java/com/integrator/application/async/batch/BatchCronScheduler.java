package com.integrator.application.async.batch;

import com.integrator.application.config.batch.BatchHelper;
import com.integrator.application.services.module.BatchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchCronScheduler {

    private final BatchDataService batchDataService;
    private final BatchHelper batchHelper;

    //60 = X = minutes
    @Scheduled(fixedDelay = 1 * 1000 * 60) // every X minutes
    public void batchDataGenerator() {
//        log.info("Generating Batch Data");
        batchDataService.generateData();
    }

    //60 = X = minutes
    @Scheduled(fixedDelay = 3 * 1000 * 60) // every X minutes
    public void batchSchedule() {
        batchHelper.runBatchDataJob();
    }
}
