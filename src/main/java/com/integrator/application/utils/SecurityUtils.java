package com.integrator.application.utils;

import com.integrator.application.models.security.Permit;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public final class SecurityUtils {

    public static boolean isAccessGranted(String... permits) {
        // lookup needed role in user roles
        final List<String> list = Arrays.asList(permits);
        final Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

//        System.out.println("isAccessGranted: "+ userAuthentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .anyMatch(list::contains));
        return userAuthentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(list::contains);
//                .anyMatch(it -> it.equals(permit));
    }

    public static void updateGrantedAuthorities(Set<Permit> permits) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<GrantedAuthority> updatedAuthorities = new HashSet<>();
        for (Permit it : permits) {
            updatedAuthorities.add(new SimpleGrantedAuthority(/*"ROLE_" +*/ it.getCode()));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    //helper for automatic security field filler
    public static String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "system";
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        }

        return username;
    }
}
