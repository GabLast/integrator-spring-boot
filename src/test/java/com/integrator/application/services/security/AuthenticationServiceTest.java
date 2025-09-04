package com.integrator.application.services.security;

import com.integrator.application.repositories.security.TokenRepository;
import com.integrator.application.services.configuration.UserSettingService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserSettingService userSettingService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthenticationService service;


}
