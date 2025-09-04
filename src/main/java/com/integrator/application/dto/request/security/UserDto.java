package com.integrator.application.dto.request.security;


import com.integrator.application.dto.BaseJson;
import lombok.Builder;

@Builder
public record UserDto(Long id, String usernameMail, String password) implements BaseJson {
}
