package com.integrator.application.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrator.application.dto.response.BadResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class FilterErrorHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    void handleError(HttpServletRequest request, HttpServletResponse response, int status,
                     String message) {
        response.setStatus(status);
        response.setContentType("application/json");
        BadResponse genericResponse = BadResponse.builder()
                .message(message)
                .path(request.getRequestURI())
                .status(status)
                .build();

        byte[] responseBody = null;
        try {
            responseBody = objectMapper.writeValueAsBytes(genericResponse);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert error message to json string: ", e);
        }

        try {
            if (responseBody != null && !response.isCommitted()) {
                response.getOutputStream().write(responseBody);
                response.flushBuffer();
            }
        } catch (IOException e) {
            log.error("Unable to write message to the response: ", e);
        }
    }
}
