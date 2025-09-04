package com.integrator.application.repositories.configuration;

import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {

    //To get the user:
//    @EntityGraph(attributePaths = {"user"})

    //Or
//    @EntityGraph(value = "UserSetting.name", type = EntityGraph.EntityGraphType.FETCH)
    UserSetting findByEnabledAndUser(boolean enabled, User user);

}
