package com.integrator.application.controllers.module;

import com.integrator.application.services.module.TestDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/testdata")
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataService service;

    @GetMapping("/findall")
//    @PreAuthorize("hasRole(T(com.integrator.application.models.security.Permit).MENU_TEST_DATA)")
//    @PreAuthorize("@securityUtils.isAccessGranted(T(com.integrator.application.models.security.Permit).MENU_TEST_DATA)")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }


}
