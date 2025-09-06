package com.integrator.application.controllers;

import com.integrator.application.dto.response.BadResponse;
import com.integrator.application.exceptions.NoAccessException;
import com.integrator.application.exceptions.ResourceExistsException;
import com.integrator.application.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class RestApiAdvise {

    @ExceptionHandler(
            exception = ResourceNotFoundException.class,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    protected BadResponse badRequest(ResourceNotFoundException ex, WebRequest request) {
        return BadResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(
            exception = ResourceExistsException.class,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.CONFLICT)
    protected BadResponse badRequest(ResourceExistsException ex, WebRequest request) {
        return BadResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(
            exception = NoAccessException.class,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    protected BadResponse badRequest(NoAccessException ex, WebRequest request) {
        return BadResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(
            exception = AccessDeniedException.class,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    protected BadResponse badRequest(AccessDeniedException ex, WebRequest request) {
        return BadResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getDescription(false))
                .build();
    }
}
