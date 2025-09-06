package com.integrator.application.dto;

import lombok.Builder;
import org.springframework.data.domain.Sort;

@Builder
public record PaginationObject(String sortProperty,
                               Sort sort,
                               Integer offset,
                               Integer limit) {
}
