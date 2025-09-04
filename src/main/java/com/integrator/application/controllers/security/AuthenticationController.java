package com.integrator.application.controllers.security;

import com.integrator.application.dto.request.security.UserDto;
import com.integrator.application.services.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(authenticationService.login(userDto), HttpStatus.OK);
    }
}
