package com.integrator.application.services.security;

import com.integrator.application.exceptions.ResourceExistsException;
import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.security.ProfileUser;
import com.integrator.application.models.security.User;
import com.integrator.application.repositories.security.UserRepository;
import com.integrator.application.services.BaseService;
import com.integrator.application.utils.OffsetBasedPageRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService extends BaseService<User, Long> {

    private final UserRepository userRepository;
    private final ProfileUserService profileUserService;
    @Getter
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    public void bootstrap() {
        User user = findByUsername("admin");
        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123"));
            user.setName("Administrator");
            user.setAdmin(true);
            saveAndFlush(user);
        }

        log.info("Created admin user");
    }

    @Transactional(readOnly = true)
    public User findByUsername(String string) {
        return userRepository.findByUsername(string);
    }

    @Transactional(readOnly = true)
    public User findByMail(String string) {
        return userRepository.findByMail(string);
    }

    @Transactional(readOnly = true)
    public User findByUsernameOrMail(String string) {
        return userRepository.findByUsernameOrMail(string);
    }

    @Transactional(readOnly = true)
    public List<User> findAllFilter(Boolean enabled, String name, String mail, Boolean admin, int limit, int offset, Sort sort) {
        return userRepository.findAllFilter(
                enabled,
                name,
                mail,
                admin,
                sort == null ? new OffsetBasedPageRequest(limit, offset) : new OffsetBasedPageRequest(limit, offset, sort)
        );
    }

    @Transactional(readOnly = true)
    public Integer countAllFilter(Boolean enabled, String name, String mail, Boolean admin) {
        return userRepository.countAllFilter(
                enabled,
                name,
                mail,
                admin
        );
    }

    public User saveUser(User user, List<ProfileUser> details, List<ProfileUser> listDelete, UserSetting userSetting) {
        user = createUser(user, userSetting);

        for (ProfileUser profileUser : listDelete) {
            profileUserService.delete(profileUser);
        }

        for (ProfileUser detail : details) {
            detail.setUser(user);
        }
        if (!details.isEmpty()) {
            profileUserService.saveAllAndFlush(details);
        }

        return user;
    }

    public User createUser(User user, UserSetting userSetting) {

        User tmp = get(user.getId()).orElse(user);

        String username = StringUtils.deleteWhitespace(user.getUsername()).toLowerCase();
        String mail = StringUtils.deleteWhitespace(user.getMail()).toLowerCase();

        if (StringUtils.isBlank(username)) {
            throw new ResourceNotFoundException("The username can not be blank");
        }

        if (StringUtils.isBlank(mail)) {
            throw new ResourceNotFoundException("The mail can not be blank");
        }

        if (findByUsername(user.getUsername()) != null && !tmp.getId().equals(user.getId())) {
            throw new ResourceExistsException("This username has already been taken. Please select a new one.");
        }

        if (findByMail(user.getMail()) != null && !tmp.getId().equals(user.getId())) {
            throw new ResourceExistsException("This mail has already been taken. Please select a new one.");
        }

        user.setUsername(username);
        user.setMail(mail);
        if (!user.getPassword().equals(tmp.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = saveAndFlush(user);

        return user;
    }

    public void delete(User user, UserSetting userSetting) {
        if (user == null) {
            throw new ResourceNotFoundException("User can not be null");
        }

        for (ProfileUser profile : profileUserService.findAllByEnabledAndUser(true, user)) {
            profileUserService.delete(profile);
        }

        disable(user);
    }

}
