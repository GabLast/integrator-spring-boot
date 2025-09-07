package com.integrator.application.models.module;


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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class TestData extends Base {

    @Column(nullable = false)
    private String word;
    private LocalDateTime date;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TestType testType;

    @Column(columnDefinition = "longtext")
    private String description;

    private BigDecimal number;
    public String toString() {
        return word;
    }

}
