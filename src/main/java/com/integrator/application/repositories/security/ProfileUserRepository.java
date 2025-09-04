package com.integrator.application.repositories.security;

import com.integrator.application.models.security.ProfileUser;
import com.integrator.application.models.security.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileUserRepository extends JpaRepository<ProfileUser, Long> {

    @EntityGraph(attributePaths = {"profile"})
    List<ProfileUser> findAllByEnabledAndUser(boolean enabled, User user);
}
