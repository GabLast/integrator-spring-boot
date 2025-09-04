package com.integrator.application.controllers.security;

import com.integrator.application.dto.request.security.UserDto;
import com.integrator.application.services.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(authenticationService.login(userDto), HttpStatus.OK);
    }

    @PostMapping(value = "/loginFlux",
            produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<?> loginReactive(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(Flux
                .just(authenticationService.login(userDto))
                .delayElements(Duration.of(2, ChronoUnit.SECONDS))
                ,HttpStatus.OK);
    }
}
