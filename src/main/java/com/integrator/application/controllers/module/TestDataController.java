package com.integrator.application.controllers.module;

import com.integrator.application.converter.PaginationConverter;
import com.integrator.application.dto.PaginationObject;
import com.integrator.application.dto.request.module.FilterTestData;
import com.integrator.application.dto.request.module.TestDataDto;
import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.module.TestData;
import com.integrator.application.services.module.TestDataModuleService;
import com.integrator.application.services.module.TestDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/testdata")
@RequiredArgsConstructor
@Slf4j
public class TestDataController {

    private final TestDataService service;
    private final TestDataModuleService testDataModuleService;

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

    @PostMapping
    @PreAuthorize("@securityUtils.isAccessGranted(T(com.integrator.application.models.security.Permit).TEST_DATA_CREATE)")
    public ResponseEntity<?> post(@RequestBody TestDataDto testDataDto) {
        TestData toSend = testDataModuleService.saveTestData(testDataDto);
        return new ResponseEntity<>(toSend, HttpStatus.OK);
    }
}
