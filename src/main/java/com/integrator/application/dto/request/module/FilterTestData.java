package com.integrator.application.dto.request.module;

import com.integrator.application.dto.BaseJson;
import com.integrator.application.dto.request.RequestPagination;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class FilterTestData extends RequestPagination implements BaseJson {
    private boolean enabled = true;
    private String word = null;
    private String description = null;
    private Long testTypeId = null;
    private LocalDate dateStart = null;
    private LocalDate dateEnd = null;
}
