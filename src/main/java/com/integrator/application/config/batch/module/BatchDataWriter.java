package com.integrator.application.config.batch.module;

import com.integrator.application.models.module.BatchData;
import com.integrator.application.services.module.BatchDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchDataWriter implements ItemWriter<Object> {

    private final BatchDataService batchDataService;

    @Override
    public void write(Chunk<? extends Object> chunk) {
        try {
            for (Object item : chunk.getItems()) {
                BatchData tmp = (BatchData) item;

                batchDataService.save(tmp);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
