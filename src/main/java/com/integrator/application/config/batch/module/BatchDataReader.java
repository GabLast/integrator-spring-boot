package com.integrator.application.config.batch.module;

import com.integrator.application.config.BatchNames;
import com.integrator.application.models.module.BatchData;
import com.integrator.application.services.module.BatchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchDataReader implements ItemReader<BatchData> {

    private final BatchDataService batchDataService;
    private Iterator<BatchData> iterator;

    @BeforeStep
    public void before(StepExecution stepExecution) {

        if (stepExecution == null) {
            return;
        }

        if (!stepExecution.getJobExecution().getJobInstance().getJobName().equals(BatchNames.JOB_BATCH_DATA)) {
            return;
        }

        getData();
    }

    @AfterChunk
    public void after(ChunkContext context) {
        if (context == null || !context.getStepContext().getJobName().equals(BatchNames.JOB_BATCH_DATA)) {
            return;
        }

        getData();
    }

    @Override
    public BatchData read() {
        if (iterator == null || !iterator.hasNext()) {
            return null;
        }

        return iterator.next();
    }

    private void getData() {
        List<BatchData> queriedData = batchDataService.findAllPendingData();

//        Disposable disposable = batchDataService.findAllPendingData()
//                .timeout(Duration.of(60, ChronoUnit.MILLIS))
//                .subscribe(queriedData::add);
//        disposable.dispose();

        iterator = queriedData.iterator();
    }
}
