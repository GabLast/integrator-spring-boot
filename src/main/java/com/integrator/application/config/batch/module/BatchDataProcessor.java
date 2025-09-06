package com.integrator.application.config.batch.module;

import com.integrator.application.models.module.BatchData;
import com.integrator.application.services.module.BatchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchDataProcessor implements ItemProcessor<Object, BatchData> {

    private final BatchDataService batchDataService;

    @Override
    public BatchData process(Object item) {
        try {
            return batchDataService.processData((BatchData) item);
        } catch (Exception e) {
            log.error("BatchDataProcessor error: " + e.getMessage());
        }
        return null;
    }
}
