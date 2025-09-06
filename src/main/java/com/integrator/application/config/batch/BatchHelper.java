package com.integrator.application.config.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static com.integrator.application.config.BatchNames.JOB_BATCH_DATA;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchHelper {

    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    private final Job jobBatchData;

    public void runBatchDataJob() {
        try {

            log.info("Running runBatchDataJob");

            Set<JobExecution> executions = jobExplorer.findRunningJobExecutions(JOB_BATCH_DATA);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.nanoTime())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(jobBatchData, jobParameters);
            executions.add(execution);

        } catch (Exception e) {
            log.error("runBatchDataJob error: " + e.getMessage());
        }
    }
}
