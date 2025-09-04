package com.integrator.application.services.configuration;

import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.security.User;
import com.integrator.application.repositories.configuration.UserSettingRepository;
import com.integrator.application.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSettingService extends BaseService<UserSetting, Long> {

    private final UserSettingRepository userSettingRepository;
    @Override
    protected JpaRepository<UserSetting, Long> getRepository() {
        return userSettingRepository;
    }

    public UserSetting findByEnabledAndUser(boolean enabled, User user) {
        return userSettingRepository.findByEnabledAndUser(enabled, user);
    }

}
