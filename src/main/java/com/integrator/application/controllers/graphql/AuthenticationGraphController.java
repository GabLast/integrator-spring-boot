package com.integrator.application.controllers.graphql;

import com.integrator.application.dto.request.security.UserDto;
import com.integrator.application.dto.response.security.LoginResponse;
import com.integrator.application.services.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class AuthenticationGraphController {

    private final AuthenticationService authenticationService;

    @MutationMapping
    public LoginResponse login(@Argument String usernameMail, @Argument String password) throws NoSuchAlgorithmException {
        UserDto dto = UserDto.builder().usernameMail(usernameMail).password(password).build();
        return authenticationService.login(dto);
    }
}
