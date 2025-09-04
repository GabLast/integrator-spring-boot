package com.integrator.application.repositories.security;

import com.integrator.application.models.security.Token;
import com.integrator.application.models.security.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @EntityGraph(value = "Token.all", type = EntityGraph.EntityGraphType.FETCH)
    Token findByTokenAndEnabledAndUser_EnabledIsTrue(String token, boolean enabled);

    Token findFirstByUserAndEnabled(User user, boolean enabled);
}
