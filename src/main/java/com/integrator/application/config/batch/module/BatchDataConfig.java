package com.integrator.application.config.batch.module;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.integrator.application.config.BatchNames.JOB_BATCH_DATA;

@Configuration
@RequiredArgsConstructor
public class BatchDataConfig {

    public static final int CHUNK_SIZE = 1;

    @Bean
    public Step stepBatchData(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager
            , BatchDataReader reader, BatchDataProcessor processor, BatchDataWriter writer
    ) {
        return new StepBuilder("stepBatchData", jobRepository)
                .chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job jobBatchData(JobRepository jobRepository,
                            Step stepBatchData,
                            BatchDataCompletionListener listener
    ) {
        return new JobBuilder(JOB_BATCH_DATA, jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepBatchData)
                .build();
    }
}
