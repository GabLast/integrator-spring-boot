//package com.integrator.application.config.r2dbc;
//
//import io.r2dbc.spi.ConnectionFactories;
//import io.r2dbc.spi.ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactoryOptions;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//
//@Configuration
//@EnableR2dbcRepositories
//@RequiredArgsConstructor
//public class R2DBCConfig extends AbstractR2dbcConfiguration {
//
//    private final R2DBCData data;
//
//    @Override
//    public ConnectionFactory connectionFactory() {
//        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
//                .option(ConnectionFactoryOptions.DRIVER, data.driver())
//                .option(ConnectionFactoryOptions.PROTOCOL, "file")
//                .option(ConnectionFactoryOptions.DATABASE, data.database())
//                .option(ConnectionFactoryOptions.USER, data.username())
//                .option(ConnectionFactoryOptions.PASSWORD, data.password())
//                .build();
//
//        return ConnectionFactories.get(options);
//    }
//}
