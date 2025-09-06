package com.integrator.application.config.r2dbc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "r2dbc")
public record R2DBCData(String database, int port, String username, String password, String driver) {
}
