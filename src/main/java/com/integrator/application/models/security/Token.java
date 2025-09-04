package com.integrator.application.models.security;

import com.integrator.application.models.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedEntityGraph(name = "Token.all", includeAllAttributes = true)
public class Token extends Base {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(unique = true)
    private String token;

    private Date expirationDate;
}
