package com.integrator.application.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrator.application.controller.AbstractControllerTest;
import com.integrator.application.dto.request.security.UserDto;
import com.integrator.application.dto.response.security.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends AbstractControllerTest {

    @Test
    void postLoginReturning200StatusCode() throws Exception {
        String json = new ObjectMapper().writeValueAsString(
                UserDto.builder()
                        .usernameMail("admin")
                        .password("123")
                        .build());

        MvcResult mvcResult = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isOk()).andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(jsonResponse, LoginResponse.class);

        Assertions.assertNotNull(response);
    }
}
