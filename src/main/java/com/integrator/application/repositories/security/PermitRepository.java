package com.integrator.application.repositories.security;

import com.integrator.application.models.security.Permit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermitRepository extends JpaRepository<Permit, Long> {

    Permit findFirstByEnabledAndCode(boolean enabled, String code);
    List<Permit> findAllByEnabled(boolean enabled);
    List<Permit> findAllByEnabledAndPermitFatherIsNull(boolean enabled);
    List<Permit> findAllByEnabledAndPermitFather(boolean enabled, Permit father);
}
