package com.integrator.application.services.security;

import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.security.Permit;
import com.integrator.application.models.security.ProfilePermit;
import com.integrator.application.models.security.ProfileUser;
import com.integrator.application.models.security.User;
import com.integrator.application.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final PermitService permitService;
    private final ProfileUserService profileUserService;
    private final ProfilePermitService profilePermitService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsernameOrMail(username);

        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                new ArrayList<>()
        );
    }

    private Set<Permit> findUserPermits(User user) {
        Set<Permit> list = new HashSet<>();

        if (user.isAdmin()) {
            list.addAll(permitService.findAllByEnabled(true));
        } else {
            //find all the profiles the user has
            List<ProfileUser> profiles = profileUserService.findAllByEnabledAndUser(true, user);
            for (ProfileUser profile : profiles) {
                //find all the permits every profile has
                list.addAll(
                        profilePermitService.findAllByEnabledAndProfile(
                                true,
                                profile.getProfile()
                        ).stream().map(ProfilePermit::getPermit).toList());
            }
        }
        return list;
    }

    public void grantAuthorities(User user) {
        if (user == null) {
            return;
        }

        Set<Permit> list = findUserPermits(user);
//            System.out.println("Authorities to be granted:");
//            for (Permit it : list) {
//                System.out.println(it.toString());
//            }
        SecurityUtils.updateGrantedAuthorities(list);
    }

    public Set<GrantedAuthority> getGrantedAuthorities(User user) {
        Set<GrantedAuthority> updatedAuthorities = new HashSet<>();
        for (Permit it : findUserPermits(user)) {
            updatedAuthorities.add(new SimpleGrantedAuthority(/*"ROLE_" +*/ it.getCode()));
        }
        return updatedAuthorities;
    }
}
