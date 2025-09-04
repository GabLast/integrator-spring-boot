package com.integrator.application.repositories.security;

import com.integrator.application.models.security.Profile;
import com.integrator.application.models.security.ProfilePermit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilePermitRepository extends JpaRepository<ProfilePermit, Long> {

    @EntityGraph(attributePaths = {"permit"})
    List<ProfilePermit> findAllByEnabledAndProfileAndPermit_EnabledIsTrueAndProfile_EnabledIsTrue(boolean enabled, Profile profile);

    List<ProfilePermit> findAllByEnabledIsTrue();
}
