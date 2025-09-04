package com.integrator.application.async;

import com.integrator.application.config.BeanNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class AsyncExecutor {

    private final SimpleAsyncTaskScheduler scheduler;

    public AsyncExecutor(@Qualifier(BeanNames.SIMPLE_ASYNC_TASK_SCHEDULER_BEAN) SimpleAsyncTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @EventListener(classes = ContextRefreshedEvent.class)
    public void async() {
        scheduler.schedule(this::simpleTest, Instant.now().plus(1, ChronoUnit.MINUTES));
    }

    private void simpleTest() {
        log.info("Simple Async Task Executed through a Scheduler");
    }
}
