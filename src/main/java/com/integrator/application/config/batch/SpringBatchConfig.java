package com.integrator.application.config.batch;

import com.integrator.application.config.BeanNames;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableBatchProcessing do not use for spring v3 or later.
@RequiredArgsConstructor
public class SpringBatchConfig {

    private final JobRepository jobRepository;

    @Bean(name = BeanNames.BATCH_JOB_LAUNCHER_BEAN)
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
