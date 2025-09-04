package com.integrator.application.dto.response.security;

import lombok.Builder;

import java.util.List;

@Builder
public record LoginResponse(String token, List<PermitDto> grantedAuthorities) {
}
