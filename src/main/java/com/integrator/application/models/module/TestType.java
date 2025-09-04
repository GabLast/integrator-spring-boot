package com.integrator.application.models.module;

import com.integrator.application.models.Base;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TestType extends Base {
    @Getter
    @AllArgsConstructor
    public enum TestTypeCode {
        TYPE_1(1, "Type 1", ""),
        TYPE_2(2, "Type 2", ""),
        TYPE_3(3, "Type 3", "");

        private final Integer code;
        private final String name;
        private final String description;
    }

    private Integer code;
    private String name;
    private String description;

    public String toString() {
        return name;
    }
}
