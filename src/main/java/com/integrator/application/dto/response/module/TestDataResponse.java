package com.integrator.application.dto.response.module;

import lombok.Builder;

import java.util.List;

@Builder
public record TestDataResponse(List<TestDataGraph> data) {
}
