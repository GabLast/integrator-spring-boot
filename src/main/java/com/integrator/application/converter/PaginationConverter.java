package com.integrator.application.converter;

import com.integrator.application.dto.PaginationObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

public class PaginationConverter {

    public static PaginationObject fromSimpleValues(String sortProperty,
                                                    String sortOrder,
                                                    Integer offset,
                                                    Integer limit) {

        sortProperty = StringUtils.isBlank(sortProperty) ? "id" : sortProperty;
        offset = offset == null ? 0 : offset;
        limit = limit == null ? 20 : limit;

        Sort sort;
        if (sortOrder.equalsIgnoreCase("DESC")) {
            sort = Sort.by(Sort.Direction.DESC, sortProperty);
        } else {
            sort = Sort.by(Sort.Direction.ASC, sortProperty);
        }

        return PaginationObject.builder()
                .sortProperty(sortProperty)
                .sort(sort)
                .offset(offset)
                .limit(limit)
                .build();
    }
}
