package com.integrator.application.dto.request;

import lombok.Data;

@Data
public abstract class RequestPagination {
    private String sortProperty = "id";
    private String sortOrder = "DESC";
    private Integer offset = 0;
    private Integer limit = 20;
}
