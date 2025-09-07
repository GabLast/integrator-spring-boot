package com.integrator.application.services.security;

import com.integrator.application.config.AppInfo;
import com.integrator.application.dto.request.security.UserDto;
import com.integrator.application.dto.response.security.LoginResponse;
import com.integrator.application.dto.response.security.PermitDto;
import com.integrator.application.exceptions.ResourceNotFoundException;
import com.integrator.application.models.configuration.UserSetting;
import com.integrator.application.models.security.Token;
import com.integrator.application.models.security.User;
import com.integrator.application.repositories.security.TokenRepository;
import com.integrator.application.services.configuration.UserSettingService;
import com.integrator.application.utils.GlobalConstants;
import com.integrator.application.utils.Utilities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticationService {

    private final AppInfo appInfo;
    private final TokenRepository tokenRepository;
    private final UserSettingService userSettingService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Transactional(readOnly = true)
    public Token findByTokenAndEnabled(String tokenHash, boolean enabled) {
        Token token = tokenRepository.findByTokenAndEnabledAndUser_EnabledIsTrue(tokenHash, enabled);

        if (token == null) {
            throw new ResourceNotFoundException("The token does not exist");
        }

        UserSetting userSetting = userSettingService.findByEnabledAndUser(true, token.getUser());

        if (userSetting == null) {
            userSetting = new UserSetting();
        }

        if (token.getExpirationDate().toInstant().isBefore(new Date().toInstant().atZone(userSetting.getTimezone().toZoneId()).toInstant())) {
            throw new ResourceNotFoundException("The token has expired");
        }

        return token;
    }

    @Transactional(readOnly = true)
    public Token findByUserAndEnabled(User user, boolean enabled) {
        return tokenRepository.findFirstByUserAndEnabled(user, enabled);
    }

    public Token generateToken(User user) throws NoSuchAlgorithmException {

        if (user == null) {
            throw new ResourceNotFoundException("User doesnt exist");
        }

        UserSetting userSetting = userSettingService.findByEnabledAndUser(true, user);
        TimeZone timeZone = userSetting != null ? userSetting.getTimezone() : TimeZone.getTimeZone(GlobalConstants.DEFAULT_TIMEZONE);

        Token token = findByUserAndEnabled(user, true);

        if (token == null) {
            token = new Token();
            token.setUser(user);

            Calendar cal = Calendar.getInstance(timeZone);
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            token.setExpirationDate(DateUtils.addDays(cal.getTime(), 7));

            token.setToken(generateBase64String(user));
            token = tokenRepository.saveAndFlush(token);
        }

        if (token.getExpirationDate().toInstant().isBefore(new Date().toInstant().atZone(timeZone.toZoneId()).toInstant())) {
            token.setEnabled(false);
            tokenRepository.saveAndFlush(token);

            token = new Token();
            token.setUser(user);

            Calendar cal = Calendar.getInstance(timeZone);
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            token.setExpirationDate(DateUtils.addDays(cal.getTime(), 7));
            token.setToken(generateBase64String(user));
            token = tokenRepository.saveAndFlush(token);
        }

        return token;
    }

    public String generateBase64String(User user) throws NoSuchAlgorithmException {
        return Base64.getEncoder()
                .encodeToString(MessageDigest.getInstance("SHA-256")
                        .digest((user.getUsername() + "/" + UUID.randomUUID())
                                .getBytes()));
    }

    public void isJWTValid(String token) {
        token = token.replace("Bearer", "").trim();

        //throws JwtException if invalid
        Jwts.parser()
                .verifyWith(Utilities.generateJWTKey(appInfo.getJwtSecretKey()))
                .build()
                .parseSignedClaims(token);
//                .getPayload();
//            System.out.println("Expires in:" + Utilities.formatDate(claims.getExpiration(),"", ""));
    }

    public String getJWTPayload(String token) {
        token = token.replace("Bearer", "").trim();

        Claims claims = Jwts.parser()
                .verifyWith(Utilities.generateJWTKey(appInfo.getJwtSecretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

//            return Utilities.decrypt(claims.get("token").toString(), Utilities.generateSecretKey());
        return claims.get("token").toString();
    }

    public String generateJWT(Token token) throws NoSuchAlgorithmException {

        if (token == null) {
            throw new ResourceNotFoundException("Could not find the server token");
        }

        if (!token.isEnabled()) {
            throw new AccessDeniedException("The token has been disabled");
        }

        UserSetting userSetting = userSettingService.findByEnabledAndUser(true, token.getUser());
        if (userSetting == null) {
            userSetting = new UserSetting();
        }

        //update the token's expiration date if it has expired.
        //if expiration date (future date) is before today (current date)
        //it has to be renewed
        //** only renew the token when required
        if (token.getExpirationDate().toInstant().isBefore(new Date().toInstant().atZone(userSetting.getTimezone().toZoneId()).toInstant())) {
            token = generateToken(token.getUser());
        }

//            String encryptedData = Utilities.encrypt(token.getToken(), Utilities.generateSecretKey());
//            String decrypted = Utilities.decrypt(token.getToken(), Utilities.generateSecretKey());

//            System.out.println("token: " + token.getToken());
//            System.out.println("encripted: " + encryptedData);
//            System.out.println("decrypted: " + decrypted);

        return Jwts.builder()
                .issuer("Integrator-Spring-Boot")
                .subject("data")
                .claim("token", token.getToken())
                .signWith(Utilities.generateJWTKey(appInfo.getJwtSecretKey()))
                .expiration(Date.from(token.getExpirationDate().toInstant().atZone(userSetting.getTimezone().toZoneId()).toInstant()))
                .compact();

    }

    public LoginResponse login(UserDto userDto) throws NoSuchAlgorithmException {
        User user = userService.findByUsernameOrMail(userDto.usernameMail());

        if (Objects.isNull(user)) {
            throw new ResourceNotFoundException("The user doesn't exist");
        }

        if (!passwordEncoder.matches(userDto.password(), user.getPassword())) {
            throw new ResourceNotFoundException("Password does not match");
        }

        Token token = generateToken(user);

        return LoginResponse.builder()
                .token(generateJWT(token))
                .grantedAuthorities(customUserDetailsService
                        .getGrantedAuthorities(user).stream()
                        .map(it -> PermitDto.builder()
                                .permit(it.getAuthority())
                                .build()).toList())
                .build();
    }


}
