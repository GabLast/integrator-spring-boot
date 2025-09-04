package com.integrator.application.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrator.application.controllers.security.AuthenticationController;
import com.integrator.application.dto.request.security.UserDto;
import com.integrator.application.dto.response.security.LoginResponse;
import com.integrator.application.services.security.AuthenticationService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(
        controllers = AuthenticationController.class,
//        excludeFilters = @ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                classes = {CustomAuthenticationFilter.class}
//        )
        excludeAutoConfiguration = {
                ReactiveSecurityAutoConfiguration.class
//                ,ReactiveUserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureWebTestClient
public class AuthenticationReactiveControllerTest {

    @Autowired
    protected WebTestClient webTestClient;
    protected static ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService service;

    @BeforeAll
    static void init() {
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    static void finish() {
        objectMapper = null;
    }

    @Test
    void postLoginFluxReturning200StatusCode() throws Exception {

        LoginResponse expectedResponse = LoginResponse.builder()
                .token("mytoken")
                .build();

        String json = new ObjectMapper().writeValueAsString(
                UserDto.builder()
                        .usernameMail("admin")
                        .password("123")
                        .build());

        when(service.login(any())).thenReturn(expectedResponse);

        webTestClient
                .post()
                .uri("/api/auth/loginFlux")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_NDJSON_VALUE))
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith((response) -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    Assertions.assertNotNull(response.getResponseBody().token());
                    Assertions.assertFalse(response.getResponseBody().token().isEmpty());
                })/*.returnResult()*/;
    }
}
