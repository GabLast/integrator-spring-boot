package com.integrator.application.models.module;

import com.integrator.application.models.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BatchData extends Base {
    @Column(nullable = false)
    private String data;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean processed;
}
