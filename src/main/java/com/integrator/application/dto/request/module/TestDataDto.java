package com.integrator.application.dto.request.module;

import com.integrator.application.dto.BaseJson;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TestDataDto(Long id, String word, String description, Long testTypeId, LocalDateTime date,
                          BigDecimal number) implements BaseJson {
}
