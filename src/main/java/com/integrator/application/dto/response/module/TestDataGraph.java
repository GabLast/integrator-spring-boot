package com.integrator.application.dto.response.module;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TestDataGraph(Long id, String word, String description, LocalDateTime date, Long testTypeId, String testType) {
}
