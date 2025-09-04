package com.integrator.application.services.security;

import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.security.Permit;
import com.integrator.application.models.security.Profile;
import com.integrator.application.models.security.ProfilePermit;
import com.integrator.application.repositories.security.ProfileRepository;
import com.integrator.application.services.BaseService;
import com.integrator.application.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProfileService extends BaseService<Profile, Long> {

    private final ProfileRepository profileRepository;
    private final ProfilePermitService profilePermitService;

    @Override
    protected JpaRepository<Profile, Long> getRepository() {
        return profileRepository;
    }

    @Transactional(readOnly = true)
    public List<Profile> findAllFilter(Boolean enabled,
                                       String name, String description,
                                       Integer limit, Integer offset, Sort sort) {
        return profileRepository.findAllFilter(
                enabled,
                name,
                description,
                sort == null ? new OffsetBasedPageRequest(limit, offset) : new OffsetBasedPageRequest(limit, offset, sort)
        );
    }


    @Transactional(readOnly = true)
    public Integer countAllFilter(Boolean enabled,
                                  String name, String description) {
        return profileRepository.countAllFilter(
                enabled,
                name,
                description
        );
    }

    public Profile create(Profile profile, List<Permit> list, UserSetting userSetting) {
        if (profile == null) {
            throw new ResourceNotFoundException("Profile is null");
        }

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException("Permits are null");
        }

        profile = saveAndFlush(profile);

        for (ProfilePermit profilePermit : profilePermitService.findAllByEnabledAndProfile(true, profile)) {
            profilePermitService.delete(profilePermit);
        }

        for (Permit permit : list) {
            profilePermitService.saveAndFlush(new ProfilePermit(profile, permit));
        }

        return profile;
    }

    public void delete(Profile profile, UserSetting userSetting) {
        if (profile == null) {
            throw new ResourceNotFoundException("Profile is null");
        }

        for (ProfilePermit profilePermit : profilePermitService.findAllByEnabledAndProfile(true, profile)) {
            profilePermitService.delete(profilePermit);
        }

        disable(profile);
    }

    @Transactional(readOnly = true)
    public List<Profile> findAllByEnabled(boolean enabled) {
        return profileRepository.findAllByEnabled(enabled);
    }
}
