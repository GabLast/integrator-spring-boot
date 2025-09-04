package com.integrator.application.services.security;

import com.integrator.application.models.security.Profile;
import com.integrator.application.models.security.ProfilePermit;
import com.integrator.application.repositories.security.ProfilePermitRepository;
import com.integrator.application.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfilePermitService extends BaseService<ProfilePermit, Long> {

    private final ProfilePermitRepository profilePermitRepository;


    @Override
    protected JpaRepository<ProfilePermit, Long> getRepository() {
        return profilePermitRepository;
    }

    public List<ProfilePermit> findAllByEnabledAndProfile(boolean enabled, Profile profile) {
        return profilePermitRepository.findAllByEnabledAndProfileAndPermit_EnabledIsTrueAndProfile_EnabledIsTrue(enabled, profile);
    }
}
