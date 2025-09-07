package com.integrator.application.controllers.module;

import com.integrator.application.converter.PaginationConverter;
import com.integrator.application.dto.PaginationObject;
import com.integrator.application.dto.request.module.FilterTestData;
import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.services.module.TestDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/testdata")
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataService service;

    @GetMapping("/findall")
//    @PreAuthorize("hasAnyAuthority('', '', '')")
//    @PreAuthorize("@securityUtils.isAccessGranted('FAKE_AUTHORITY')")
//    @PreAuthorize("@securityUtils.isAccessGranted(T(com.integrator.application.models.security.Permit).MENU_TEST_DATA, T(com.integrator.application.models.security.Permit).MENU_TEST_DATA)")
    @PreAuthorize("@securityUtils.isAccessGranted(T(com.integrator.application.models.security.Permit).MENU_TEST_DATA)")
    public ResponseEntity<?> findAll(FilterTestData filterTestData) {

        UserSetting userSetting = (UserSetting) SecurityContextHolder.getContext().getAuthentication().getDetails();
        PaginationObject paginationObject = PaginationConverter.fromSimpleValues(
                filterTestData.getSortProperty(),
                filterTestData.getSortOrder(),
                filterTestData.getOffset(),
                filterTestData.getLimit()
        );

        return new ResponseEntity<>(service.findAllFilter(
                filterTestData.isEnabled(),
                userSetting != null ? userSetting.getTimeZoneString() : null,
                filterTestData.getWord(),
                filterTestData.getDescription(),
                null,
                filterTestData.getTestTypeId(),
                filterTestData.getDateStart(),
                filterTestData.getDateEnd(),
                paginationObject.limit(),
                paginationObject.offset(),
                paginationObject.sort()
        ), HttpStatus.OK);
    }

}
