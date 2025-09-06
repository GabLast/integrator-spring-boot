package com.integrator.application.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityBeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    https://www.baeldung.com/spring-security-jdbc-authentication
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http,
//                                                       DataSource dataSource,
//                                                       CustomUserDetailsService customUserDetailsService,
//                                                       PasswordEncoder passwordEncoder
//    ) throws Exception {
//
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        authenticationManagerBuilder
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("query")
//                .passwordEncoder(passwordEncoder)
//        ;
//
//        return authenticationManagerBuilder.build();
//    }
}
