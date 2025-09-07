package com.integrator.application.controllers.graphql;

import com.integrator.application.converter.PaginationConverter;
import com.integrator.application.dto.PaginationObject;
import com.integrator.application.dto.response.module.TestDataGraph;
import com.integrator.application.dto.response.module.TestDataResponse;
import com.integrator.application.models.module.TestData;
import com.integrator.application.services.module.TestDataService;
import com.integrator.application.utils.GlobalConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestDataGraphController {

    private final TestDataService testDataService;

    @QueryMapping
//    @PreAuthorize("@securityUtils.isAccessGranted('FAKE_AUTHORITY')")
    @PreAuthorize("@securityUtils.isAccessGranted(T(com.integrator.application.models.security.Permit).MENU_TEST_DATA)")
    public List<TestDataGraph> findAllFilter(@Argument boolean enabled,
                                             @Argument String word,
                                             @Argument String description,
                                             @Argument Long testTypeId,
                                             @Argument LocalDate dateStart,
                                             @Argument LocalDate dateEnd,
                                             @Argument String sortProperty,
                                             @Argument String sortOrder,
                                             @Argument Integer offset,
                                             @Argument Integer limit
    ) {

        PaginationObject paginationObject = PaginationConverter.fromSimpleValues(
                sortProperty,
                sortOrder,
                offset,
                limit
        );

        List<TestData> rawList = testDataService.findAllFilter(
                enabled,
                GlobalConstants.DEFAULT_TIMEZONE,
                word,
                description,
                null,
                testTypeId,
                dateStart,
                dateEnd,
                paginationObject.limit(),
                paginationObject.offset(),
                paginationObject.sort()
        );

        List<TestDataGraph> cleanList = rawList.stream().map(it -> TestDataGraph.builder()
                .id(it.getId())
                .word(it.getWord())
                .description(it.getDescription())
                .date(it.getDate())
                .testTypeId(it.getTestType().getId())
                .testType(it.getTestType().getName())
                .build()).toList();

        TestDataResponse response = TestDataResponse.builder()
                .data(cleanList)
                .build();

        return cleanList;
    }
}
