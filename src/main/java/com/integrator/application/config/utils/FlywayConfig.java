package com.integrator.application.config.utils;

import com.integrator.application.config.AppInfo;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//@Configuration
@Component
@RequiredArgsConstructor
public class FlywayConfig {

    private final AppInfo appInfo;
    private final Flyway flyway;

    @EventListener(ApplicationReadyEvent.class)
    public void migrate() {
        if(!appInfo.getProfile().equalsIgnoreCase("prod")) {
            flyway.migrate();
        }
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // do nothing
        };
    }

}
