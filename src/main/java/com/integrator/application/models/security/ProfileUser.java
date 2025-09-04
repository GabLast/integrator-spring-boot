package com.integrator.application.models.security;

import com.integrator.application.models.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProfileUser extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
