package com.integrator.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class AsyncConfig {

    @Bean(name = BeanNames.SIMPLE_ASYNC_TASK_SCHEDULER_BEAN)
    SimpleAsyncTaskScheduler simpleTaskExecutor() {
        SimpleAsyncTaskScheduler scheduler = new SimpleAsyncTaskScheduler();
        scheduler.setVirtualThreads(true);
        scheduler.setThreadNamePrefix("SimpleAsyncTaskScheduler-");
        return scheduler;
    }

    @Bean(name = BeanNames.ASYNC_TASK_EXECUTOR_BEAN)
    Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);      // min threads
        executor.setMaxPoolSize(5);       // max
        executor.setQueueCapacity(100);   // queue
        executor.setThreadNamePrefix("TaskExecutor-");
        executor.initialize();

        return executor;
    }
}
