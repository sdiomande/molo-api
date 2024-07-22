package com.map.moloapi.services;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.users.requests.AuthenticateRequest;
import com.map.moloapi.contracts.users.requests.RefreshTokenRequest;
import com.map.moloapi.contracts.users.requests.ResetPasswordRequest;
import com.map.moloapi.contracts.users.responses.AuthResponse;
import com.map.moloapi.entities.*;
import com.map.moloapi.exceptions.rest.BadRequestException;
import com.map.moloapi.repositories.MenuRepository;
import com.map.moloapi.repositories.ProfilRepository;
import com.map.moloapi.repositories.UserRepository;
import com.map.moloapi.securities.JwtUtils;
import com.map.moloapi.securities.UserDetailsImpl;
import com.map.moloapi.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.map.moloapi.utils.constants.FunctionalMessage.PASSWORD_NOT_MATCHING;
import static com.map.moloapi.utils.constants.TechnicalMessage.*;

/**
 * @author DIOMANDE Souleymane
 * @Date 19/11/2022 00:37
 */
@Service
@Slf4j
public class AuthService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfilRepository profilRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    Utilities utilities;

    @Autowired
    ResetTokenService resetTokenService;

    Response response = new Response();

    public final static String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public Response login(AuthenticateRequest loginRequest) {
        log.info("-- Debut authentification, login : {} --", loginRequest.getLogin());
        Map<String, Object> map = new HashMap<>();
        Response.ResponseBuilder responseDtoBuilder = Response.builder();
        responseDtoBuilder.hasError(true);

        User userToVerify = userRepository.findByLogin(loginRequest.getLogin());
        if (userToVerify == null) {
            log.info("-- Acces incorrect --");
            responseDtoBuilder.message(BAD_CREDENTIAL);
            return responseDtoBuilder.build();
        }
        if (userToVerify.getLocked()) {
            log.info("-- Compte bloqué --");
            responseDtoBuilder.message(ACCOUNT_LOCKED)
                    .data(new HashMap() {
                        {
                            put("accountBlocked", true);
                        }
                    });
            return responseDtoBuilder.build();
        }

        if (!userToVerify.isActive()) {
            log.info("-- Compte inactif --");
            responseDtoBuilder.message(ACCOUNT_DISABLE)
                    .data(new HashMap() {
                        {
                            put("accountDisabled", true);
                        }
                    });
            return responseDtoBuilder.build();
        }

        if (userToVerify.passwordExpired()) {
            log.info("-- Mot de passe expiré --");
            return responseDtoBuilder.message(PASSWORD_EXPIRED)
                    .data(new HashMap() {
                        {
                            put("passwordExpired", true);
                        }
                    })
                    .build();
        }

        if (userToVerify.getRole() == null) {
            throw new BadRequestException("Veuillez contactez l'administrateur pour vous attribuer un role");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), Utilities.arroundPassword(loginRequest.getPassword())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userToVerify.getId());
            resetFailedAttemptLogin(userToVerify);

            map.put("id", userToVerify.getId());
            map.put("firstName", userToVerify.getFirstName());
            map.put("lastName", userToVerify.getLastName());
            map.put("login", userToVerify.getLogin());
            map.put("email", userToVerify.getEmail());
            map.put("firstConnexion", userToVerify.getFirstConnexion());
            map.put("attempt", userToVerify.getAttempt());
            map.put("locked", userToVerify.getLocked());
            map.put("sessionTimeOut", utilities.getParam("SESSION_TIMEOUT"));
            map.put("refreshToken", refreshToken.getToken());
            map.put("role", userToVerify.getRole().getLibelle());
            // load menu and profil
            List<Profil> profils = profilRepository.findByRole(userToVerify.getRole());
            if (!profils.isEmpty()) {
                List<Menu> menus = profils.stream().map(Profil::getChild).collect(Collectors.toList());
                map.put("menus", menus);
            }

            // setFirstConnexion to false
//            userRepository.updateFirstConnection(false, userToVerify.getId());

            responseDtoBuilder.hasError(false)
                    .message(SUCCESS_MESSAGE)
                    .data(new HashMap() {
                        {
                            put("token", jwtUtils.generateJwtToken(map, userToVerify.getLogin()));
                        }
                    })
                    .build();
            log.info("-- Fin authentification --");

        } catch (BadCredentialsException e) {
            if (userToVerify == null) {
                log.info("-- Acces incorrect, aucun utilisateur trouve --");
                responseDtoBuilder.message(BAD_CREDENTIAL);
            }
            Integer loginAttemptMax = Integer.valueOf(utilities.getParam("LOGIN_ATTEMPT_MAX", "3"));
            if ((userToVerify.getAttempt() + 1) >= loginAttemptMax) {
                log.info("-- Compte verouille --");
                //Verrouiler le compte
                userToVerify.setAttempt(loginAttemptMax);
                userToVerify.setLocked(true);
                userRepository.saveAndFlush(userToVerify);
//                throw new RuntimeException(ACCOUNT_LOCKED);
                responseDtoBuilder.message(ACCOUNT_LOCKED);
            } else {
                log.info("-- Acces incorrect, tentative de connexion : {} --", userToVerify.getAttempt() + 1);
                userToVerify.setAttempt(userToVerify.getAttempt() + 1);
                userRepository.saveAndFlush(userToVerify);
//                throw new RuntimeException(BAD_CREDENTIAL);
                responseDtoBuilder.message(BAD_CREDENTIAL);
            }
        }
        return responseDtoBuilder.build();
    }

    public Response resetPassword(ResetPasswordRequest request) {
        log.info("-- Debut reinitialisation de mot de passe --");

        // Readapter la logique.
        // pour change password forgottent
        // retreive user by token
        // check if isnt expired
        // check password match
        // update
        // attempt -> 0
        // if ok -> redirect login page

        //confirm password

        // check regex password
        if (request.getNewPassword() != null){
            if (!request.getNewPassword().matches(PASSWORD_REGEX)){
                log.error("-- Le nouveau mot de passe ne respectent pas les regles de securité --");
                return response.error("Le nouveau mot de passe ne respectent pas les regles de securité");
            }
        }

        if (request.getConfirmPassword() != null){
            if (!request.getConfirmPassword().matches(PASSWORD_REGEX)){
                log.error("-- Le nouveau mot de passe ne respectent pas les regles de securité --");
                return response.error("Le nouveau mot de passe ne respectent pas les regles de securité");
            }
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.error("-- Les nouveaux mots de passe ne sont pas identique --");
            return response.error("Mot de passe non identique");
        }
        //Check if user existing
        User user = resetTokenService.verifyTokenUser(request.getToken());
//        AppUser user = userRepository.findByLogin(request.getPhoneNumber());
        if (user != null) {
//            if (user.isActive()) {
            // change password
            user.setPassword(Utilities.encryptPassword(request.getConfirmPassword()));
            user.setUpdatedAt(Utilities.now());
            userRepository.saveAndFlush(user);

            // force expiry token, set passed date
            resetTokenService.forceTokenExpiry(request.getToken());
            log.info("-- Fin reinitialisation de mot de passe --");

            return Response.builder()
                    .hasError(false)
                    .message(SUCCESS_MESSAGE)
                    .build();
//            } else {
//                //Contact l'admin car compte desactive
//                log.error("########## END RESET PASSWORD, ACCOUNT DISABLE ##########");
//                throw new RuntimeException(ACCOUNT_DISABLE);
//            }
        } else {
            // No data found, Erreur est survenu
            log.error("-- Fin reinitialisation de mot de passe, aucune donne trouve --");
//            throw new RuntimeException(ERROR_MESSAGE);
            return response.error();
        }
    }

    private void resetFailedAttemptLogin(User user) {
        if (user.getAttempt() != 0 || user.getLocked()) {
            user.setAttempt(0);
            user.setLocked(false);
            userRepository.saveAndFlush(user);
        }
    }

    public Response refreshToken(RefreshTokenRequest request) {
        log.info("-- Debut rafraichissement token --");

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(u -> {
//                    String token = jwtUtils.generateTokenFromUsername(u.getLogin());
//                    String tokenx = jwtUtils.generateJwtToken(u);
                    refreshTokenService.deleteRefreshToken(u.getId());
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(u.getId());

                    AuthResponse authResponse = AuthResponse.builder()
                            .sub(u.getLogin())
                            .sessionTimeout(Long.valueOf(utilities.getParam("TTL")))
                            .refreshToken(refreshToken.getToken())
                            .role(u.getRole().getLibelle())
                            .id(u.getId())
                            .firstName(u.getFirstName())
                            .lastName(u.getLastName())
                            .phoneNumber(u.getPhoneNumber())
                            .email(u.getEmail())
                            .firstConnexion(u.getFirstConnexion())
                            .login(u.getLogin())
                            .attempt(u.getAttempt())
                            .locked(u.getLocked())
                            .build();

                    String token = jwtUtils.generateJwtToken(Utilities.authenticationConnected(), authResponse);
                    log.info("-- Fin rafraichissement token --");

                    return Response.builder()
                            .hasError(false)
                            .message(SUCCESS_MESSAGE)
                            .data(token)
                            .build();
                })
                .orElseThrow(() -> {
                            log.error("## Le token de rafraichissement n'existe pas. Token : {}", requestRefreshToken);
                            return new RuntimeException(ERROR_MESSAGE);
                        }
                );
    }

    public Response verifyResetPasswordToken(String token) {
        return resetTokenService.verifyToken(token);
    }
}
