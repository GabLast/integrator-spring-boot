//package com.integrator.application.filters;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.integrator.application.services.configuration.UserSettingService;
//import com.integrator.application.services.security.AuthenticationService;
//import com.integrator.application.services.security.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class FilterConfig {
//    //Fix to Vaadin blocking post requests.
//    //
//    //https://stackoverflow.com/questions/79364651/update-spring-boot-3-4-1-spring-security-error-a-filter-chain-that-matches-any
//    private final CustomUserDetailsService customUserDetailsService;
//    private final AuthenticationService authenticationService;
//    private final UserSettingService userSettingService;
//    private final ObjectMapper objectMapper;
//
//    @Bean
//    public FilterRegistrationBean<CustomAuthenticationFilter> afterAuthFilterRegistrationBean(
//            SecurityProperties securityProperties) {
//
//        var filterRegistrationBean = new FilterRegistrationBean<CustomAuthenticationFilter>();
//
//        // a filter that extends OncePerRequestFilter
//        filterRegistrationBean.setFilter(new CustomAuthenticationFilter(objectMapper, authenticationService, customUserDetailsService, userSettingService));
//
//        // this needs to be a number greater than than spring.security.filter.order
//        filterRegistrationBean.setOrder(securityProperties.getFilter().getOrder() + 1);
//        return filterRegistrationBean;
//    }
//}
