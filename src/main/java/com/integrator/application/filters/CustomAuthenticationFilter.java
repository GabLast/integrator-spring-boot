package com.integrator.application.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrator.application.config.security.CustomAuthentication;
import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.security.Token;
import com.integrator.application.models.security.User;
import com.integrator.application.services.configuration.UserSettingService;
import com.integrator.application.services.security.AuthenticationService;
import com.integrator.application.services.security.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
@Slf4j
public class CustomAuthenticationFilter extends FilterErrorHandler {
    //    filters for fully webflux: https://www.baeldung.com/spring-webflux-filters
    // tldr: class ExampleWebFilter implements WebFilter
    private final AuthenticationService authenticationService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserSettingService userSettingService;

    public CustomAuthenticationFilter(ObjectMapper objectMapper, AuthenticationService authenticationService, CustomUserDetailsService customUserDetailsService, UserSettingService userSettingService) {
        super(objectMapper);
        this.authenticationService = authenticationService;
        this.customUserDetailsService = customUserDetailsService;
        this.userSettingService = userSettingService;
    }

    //    https://www.geeksforgeeks.org/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        String authToken = request.getHeader("Authorization");
//            System.out.println("For Request: " + request.getServletPath() + "\n\n");
//            System.out.println("Token: " + authToken + "\n\n");
        try {
            if (StringUtils.isBlank(authToken)) {
                throw new ResourceNotFoundException("The request did not send its Authorization token");
            }

            authenticationService.isJWTValid(authToken);

            String payload = authenticationService.getJWTPayload(authToken);
            if (StringUtils.isBlank(payload)) {
                throw new AccessDeniedException("Invalid token payload");
            }

            Token token = authenticationService.findByTokenAndEnabled(payload, true);

            CustomAuthentication authentication = new CustomAuthentication(
                    token,
                    customUserDetailsService.getGrantedAuthorities(token.getUser()),
                    userSettingService.findByEnabledAndUser(true, token.getUser())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) {
                throw new RuntimeException("SecurityContextHolder User is null");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            if (e instanceof AccessDeniedException ||
                    e instanceof ResourceNotFoundException) {
                handleError(request, response, HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            } else if (e instanceof JwtException) {
                handleError(request, response, HttpStatus.BAD_REQUEST.value(), "Invalid JWT");
            } else {
                handleError(request, response, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
//        https://stackoverflow.com/questions/52370411/springboot-bypass-onceperrequestfilter-filters
        boolean filter;

        filter =
                new AntPathMatcher().match("/api/auth/**", request.getServletPath()) ||
                        new AntPathMatcher().match("/sw.js", request.getServletPath()) ||
                        new AntPathMatcher().match("/*.ico", request.getServletPath()) ||
                        new AntPathMatcher().match("/dbconsole", request.getServletPath()) ||
                        new AntPathMatcher().match("/dbconsole/**", request.getServletPath())
        ;
//        System.out.println("For Request: " + request.getServletPath() + "\t | " + filter);
        return filter;
    }

}
