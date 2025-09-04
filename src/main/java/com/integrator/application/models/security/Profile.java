package com.integrator.application.models.security;

import com.integrator.application.models.Base;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Profile extends Base {

    private String name;
    private String description;

    public String toString() {
        return name;
    }

}
