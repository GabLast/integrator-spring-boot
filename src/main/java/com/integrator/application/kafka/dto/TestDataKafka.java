package com.integrator.application.kafka.dto;

import com.integrator.application.models.module.TestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDataKafka {
    private Long id;
    private String word;
    private String description;
    private TestType testType;
    private BigDecimal number;
}
