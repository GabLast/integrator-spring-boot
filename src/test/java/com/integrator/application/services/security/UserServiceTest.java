package com.integrator.application.services.security;

import com.integrator.application.models.security.User;
import com.integrator.application.repositories.security.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;

    @Test
    void testFindByNameAndEnabled() {

        String username = "user";
        User user = User.builder()
                .username("user")
                .mail("mail1@mail.com")
                .build();

        when(repository.findByUsernameOrMail(username)).thenReturn(user);

        User test = service.findByUsernameOrMail(username);

        Assertions.assertNotNull(test);
    }
}
