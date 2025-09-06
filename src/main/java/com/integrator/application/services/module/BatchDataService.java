package com.integrator.application.services.module;

import com.integrator.application.models.module.BatchData;
import com.integrator.application.repositories.module.BatchDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BatchDataService {

    private final BatchDataRepository repository;

    public BatchData save(BatchData data) {
        return repository.saveAndFlush(data);
    }

    public BatchData getByIdAndEnabled(Long id, boolean enabled) {
        return repository.getByIdAndEnabled(id, enabled);
    }

    public List<BatchData> findAllPendingData() {
        return repository.findAllByProcessedAndEnabled(false, true);
    }

    public BatchData processData(BatchData data) {
        try {
            BatchData value = getByIdAndEnabled(data.getId(), true);
            if (value == null) {
                return null;
            }
            value.setProcessed(true);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    public void generateData() {
        repository.save(BatchData.builder()
                .data(UUID.randomUUID().toString())
                .build());
    }
}
