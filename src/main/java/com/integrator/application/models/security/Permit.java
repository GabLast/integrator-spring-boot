package com.integrator.application.models.security;

import com.integrator.application.models.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Permit extends Base {

    public static final String PROCESSES_MODULE = "PROCESSES_MODULE";

    public static final String MENU_TEST_DATA = "MENU_TEST_DATA";
    public static final String TEST_DATA_CREATE = "TEST_DATA_CREATE";
    public static final String TEST_DATA_EDIT = "TEST_DATA_EDIT";
    public static final String TEST_DATA_VIEW = "TEST_DATA_VIEW";
    public static final String TEST_DATA_DELETE = "TEST_DATA_DELETE";

    //***********************************************************************************
    public static final String SECURITY_MODULE = "SECURITY_MODULE";

    public static final String MENU_PROFILE = "MENU_PROFILE";
    public static final String PROFILE_CREATE = "PROFILE_CREATE";
    public static final String PROFILE_EDIT = "PROFILE_EDIT";
    public static final String PROFILE_VIEW = "PROFILE_VIEW";
    public static final String PROFILE_DELETE = "PROFILE_DELETE";

    public static final String MENU_USER = "MENU_USER";
    public static final String USER_CREATE = "USER_CREATE";
    public static final String USER_EDIT = "USER_EDIT";
    public static final String USER_VIEW = "USER_VIEW";
    public static final String USER_DELETE = "USER_DELETE";
    public static final String USER_TOKEN = "USER_TOKEN";

    //***********************************************************************************
    public static final String REPORTS_MODULE = "REPORTS_MODULE";
    public static final String REPORT_TEST_DATA = "REPORT_TEST_DATA";

    @ManyToOne(fetch = FetchType.LAZY)
    private Permit permitFather;

    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    private String description;

    public String toString() {
        return name;
    }
}
