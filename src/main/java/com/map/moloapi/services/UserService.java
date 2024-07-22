package com.map.moloapi.services;


import com.map.moloapi.caches.LogoutCache;
import com.map.moloapi.contracts.ExpiryToken;
import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.users.requests.CreateUserRequest;
import com.map.moloapi.contracts.users.requests.UpdatePasswordRequest;
import com.map.moloapi.contracts.users.requests.UpdateUserRequest;
import com.map.moloapi.contracts.users.responses.AuthResponse;
import com.map.moloapi.dtos.UserDto;
import com.map.moloapi.entities.Entreprise;
import com.map.moloapi.entities.PasswordResetToken;
import com.map.moloapi.entities.Role;
import com.map.moloapi.entities.User;
import com.map.moloapi.mappers.BaseMapper;
import com.map.moloapi.repositories.EntrepriseRepository;
import com.map.moloapi.repositories.RefreshTokenRepository;
import com.map.moloapi.repositories.RoleRepository;
import com.map.moloapi.repositories.UserRepository;
import com.map.moloapi.securities.JwtUtils;
import com.map.moloapi.utils.Utilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.map.moloapi.utils.constants.FunctionalMessage.OLD_PASSWORD_WRONG;
import static com.map.moloapi.utils.constants.FunctionalMessage.PASSWORD_NOT_MATCHING;
import static com.map.moloapi.utils.constants.TechnicalMessage.SUCCESS_MESSAGE;

/**
 * @author DIOMANDE Souleymane
 * @Date 04/10/2022 20:04
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    BaseMapper mapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ResetTokenService resetTokenService;

    @Autowired
    LogoutCache logoutCache;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    EntrepriseRepository entrepriseRepository;

    @Autowired
    Utilities utils;

    @Autowired
    NotificationService notificationService;

    Response response = new Response();

    public Response create(CreateUserRequest request) {
        CreateUserRequest cloneReq = request.clone();
        cloneReq.setPassword("****");
        log.info("-- Debut creation utilisateur : {} --", cloneReq);
        Optional<Role> role = roleRepository.findById(request.getRoleId());
        User user = mapper.toEntity(request);
        if (role.isEmpty()) {
            log.error("-- Aucun role trouvé --");
            return response.error("Aucun role trouvé");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            log.error("-- Aucun mot de passe saisi --");
            return response.error("Aucun mot de passe saisi");
        }


        if (userRepository.existsByLogin(request.getLogin())) {
            log.error("-- Le login existe deja : {} --", request.getLogin());
            return response.error("Le login existe deja : " + request.getLogin());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("-- L'email existe deja : {} --", request.getEmail());
            return response.error("L'email existe deja : " + request.getEmail());
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            log.error("-- Le numero de telephone existe deja : {} --", request.getPhoneNumber());
            return response.error("Le numero de telephone existe deja : " + request.getPhoneNumber());
        }

        if (role.get().getLibelle().equalsIgnoreCase("ROLE_PARTENAIRE")) {
            if (request.getPartnerId() == null) {
                log.error("## L'utilisateur n'est pas affecte a une entreprise ##");
                return response.error("L'utilisateur n'est pas affecte a une entreprise");
            }

            Optional<Entreprise> partnerOptional = entrepriseRepository.findById(request.getPartnerId());
            if (partnerOptional.isEmpty()) {
                log.error("## Aucune entreprise trouvé ##");
                return response.error("Aucune entreprise trouvé");
            }

            user.setEntreprise(partnerOptional.get());
        }
//        String password = Utilities.generateDigitPassword();
//        request.setPassword(Utilities.encryptPassword(password));
//        user.setLogin(request.getPhoneNumber());
        user.setRole(role.get());
//        user.setPartner(partnerOptional.get());
        user.cAudit();
        user.setPassword(Utilities.encryptPassword(request.getPassword()));
        user.setExpiredAt(Utilities.addDays(Integer.parseInt(utils.getParam("PASSWORD_TTL"))));
        User userSaved = userRepository.saveAndFlush(user);

        try {
            notificationService.accountCreation(userSaved, request.getPassword());
        } catch (Exception e) {
            log.error("UNE ERREUR EST SURVENU LORS DE L'ENVOI DE LA NOTIFICATION DE LA CREATION DE COMPTE");
            e.printStackTrace();
        }
        log.info("-- Fin creation utilisateur --");
        return response.success(mapper.toDto(userSaved));
    }

    public Response update(UpdateUserRequest request, String id) {
        log.info("-- Debut modification utilisateur : {} --", request);
        Optional<User> userOptional = userRepository.findById(id);
        return update(request, userOptional);
    }

    public Response activate(String id) {
        log.info("-- Debut activation/desactivation utilisateur --, id : {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userToUpdate = user.get();
            if (userToUpdate.isActive()) {
                log.info("-- Desactivation --");
            } else {
                log.info("-- Activation --");
            }
            userToUpdate.setActive(!user.get().isActive()); // Activate or desactive user
            userToUpdate.activatedAudit();
            userToUpdate.uAudit();
            log.info("-- Fin activation/desactivation utilisateur --");
            return response.success(mapper.toDto(userRepository.save(userToUpdate)));
        }
        log.error("-- Aucun utilisateur trouvé --");
        return response.error("Aucun utilisateur trouvé ");
    }

    public Response updatePassword(UpdatePasswordRequest request) {
        log.info("-- Debut mise a jour mot de passe --");

        //confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.error("-- {} --", PASSWORD_NOT_MATCHING);
            return response.error(PASSWORD_NOT_MATCHING);
        }

        Optional<User> userConnected = userRepository.findById(Utilities.userConnectedID());
        User user = userConnected.get();
        if (!Utilities.match(request.getPassword(), user.getPassword())) {
            log.error("-- {} --", OLD_PASSWORD_WRONG);
            return response.error(OLD_PASSWORD_WRONG);
        }
        if (user != null) {
            user.setPassword(Utilities.encryptPassword(request.getConfirmPassword()));
            user.setFirstConnexion(false);
            user.uAudit();
            userRepository.saveAndFlush(user);

            log.info("-- Fin mise a jour mot de passe --");
            return response.success(mapper.toDto(user));
        }
        log.error("-- Fin mise a jour mot de passe --");
        return response.error();
    }


    public Response all() {
        log.info("-- Debut recuperation des utilisateurs --");
        List<UserDto> users = mapper.toUserDtos(userRepository.findAll());
        log.info("-- Fin recuperation des utilisateurs --");
        return response.success(users);
    }

    public Response locked() {
        log.info("-- Debut recuperation des utilisateurs bloqués --");
        List<UserDto> users = mapper.toUserDtos(userRepository.findLocked());
        log.info("-- Fin recuperation des utilisateurs bloqués --");
        return response.success(users);
    }

    public Response findById(String id) {
        log.info("-- Debut recuperation des details d'un utilisateur, id : {} --", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.error("-- Aucun utilisateur trouvé --");
            return response.error("Aucun utilisateur trouvé");
        }
        log.info("-- Fin recuperation des details d'un utilisateur --");
        return response.success(mapper.toDto(user.get()));
    }

    public Response resetPasswordx(String id) {
        log.info("-- Debut reinitialisation de mot de passe, id : {} --", id);
        Optional<User> userToReset = userRepository.findById(id);
        if (userToReset.isEmpty()) {
            log.error("-- Aucun utilisateur trouvé --");
            return response.error("Aucun utilisateur trouvé");
        }

        //check if reset password email already sended to user
        ExpiryToken expiryToken = resetTokenService.tokenHasExpiry(userToReset.get());
        if (!expiryToken.isExpiry()) {
            throw new RuntimeException(expiryToken.getMessage());
        }
        // build resetUrl
        PasswordResetToken passwordResetToken = resetTokenService.buildResetUrl(userToReset.get());

        try {
//            notificationService.sendNotificationPasswordReset(EmailDetailRequest.builder()
//                    .to(new String[]{userToReset.get().getEmail()})
//                    .resetUrl(passwordResetToken.getUrl())
//                    .ttl(utilities.getParam(ParamEnum.TTL.getValue()).concat(" minutes"))
//                    .build());
        } catch (Exception e) {
            log.error("UNE ERREUR EST SURVENU LORS DE L'ENVOI DE LA NOTIFICATION DE LA REINITIALISATION DE COMPTE");
            e.printStackTrace();
        }
        log.info("-- END RESET USER --");
        return Response.builder()
                .hasError(false)
                .message(SUCCESS_MESSAGE)
                .build();
    }

    public Response resetPassword(String id) {
        log.info("-- Debut reinitialisation de mot de passe, id : {} --", id);
        Optional<User> userToReset = userRepository.findById(id);
        if (userToReset.isEmpty()) {
            log.error("-- Aucun utilisateur trouvé --");
            return response.error("Aucun utilisateur trouvé");
        }
        //check if reset password email already sended to user
        ExpiryToken expiryToken = resetTokenService.tokenHasExpiry(userToReset.get());
        if (!expiryToken.isExpiry()) {
            log.error("-- {} --", expiryToken.getMessage());
            return response.error(expiryToken.getMessage());
//            throw new RuntimeException(expiryToken.getMessage());
        }
        // build resetUrl
        String resetUrl = resetTokenService.getResetUrl(userToReset.get());

        String password = Utilities.generateSecurePassword();
        userToReset.get().setPassword(Utilities.encryptPassword(password));
//        User userSaved = userRepository.saveAndFlush(userToReset.get());
        try {
            notificationService.passwordReset(userToReset.get(), password, resetUrl);
        } catch (Exception e) {
            log.error("UNE ERREUR EST SURVENU LORS DE L'ENVOI DE LA NOTIFICATION DE LA REINITIALISATION DE COMPTE");
            e.printStackTrace();
        }
        log.info("-- Fin reinitialisation de mot de passe --");
        return response.success();
    }

    public Response unlock(String id) {
        log.info("-- Debut deblocage utilisateur, id : {} --", id);
        Optional<User> userToUnlock = userRepository.findById(id);
        if (userToUnlock.isEmpty()) {
            log.error("-- Aucun utilisateur trouvé , id : {}  --", id);
            return response.error("Aucun utilisateur trouvé");
        }
        if (!userToUnlock.get().getLocked()) {
            log.error("-- Utilisateur non bloqué , id : {}  --", id);
            return response.error("L'utilisateur n'est pas bloqué");
        }

        User user = userToUnlock.get();
        user.setLocked(false);
        user.setAttempt(0);
        user.uAudit();
        userRepository.save(user);

        log.info("-- Fin deblocage utilisateur --");
        return response.success();
    }

    public Response me() {
        log.info("-- Debut recuperation des informations du profile --");
        UserDto userDto = mapper.toDto(Utilities.userConnected());
        log.info("-- Fin recuperation des informations du profile --");
        return response.success(userDto);
    }

    public Response updateMe(UpdateUserRequest request) {
        log.info("-- Debut mise a jour profile --");
        Optional<User> userOptional = userRepository.findById(Utilities.userConnectedID());
        return update(request, userOptional);
    }

    public Response logout(HttpServletRequest request) {
        log.info("-- Debut deconnexion --");
        try {
            String token = parseJwt(request);
            AuthResponse authResponse = jwtUtils.getAuthResponseFromToken(token);
            User user = userRepository.findByLogin(authResponse.getLogin());
            if (user != null) {
                logoutCache.markLogoutEventForToken(user, token);
                // delete user refresh token
                refreshTokenRepository.deleteByUserId(user.getId());
                request.logout();
            }
            log.info("-- Fin deconnexion --");
            return response.success();
        } catch (ServletException e) {
            log.error("-- Erreur lors de la deconnexion : {} --", e.getMessage());
            return response.error();
        }
    }

    public Response delete(String id) {
        log.info("-- Debut suppression utilisateur, id : {} --", id);
        Optional<User> userToUnlock = userRepository.findById(id);
        if (userToUnlock.isEmpty()) {
            log.error("-- Aucun utilisateur trouvé , id : {}  --", id);
            return response.error("Aucun utilisateur trouvé");
        }
        if (userToUnlock.get().getDeletedAt() != null) {
            log.info("-- Utilisateur deja supprime --");
            return response.success();
        }

        User user = userToUnlock.get();
        user.dAudit();
        userRepository.save(user);

        log.info("-- Fin suppression utilisateur --");
        return response.success();
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        throw new RuntimeException("Token invalide");
    }

    private Response update(UpdateUserRequest request, Optional<User> userOptional) {
        if (userOptional.isEmpty()) {
            log.error("-- Aucun utilisateur trouvé --");
            return response.error();
        }
        User user = userOptional.get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

//        if (userRepository.loginNotUpdatable(user.getId(), request.getLogin())) {
//            log.error("-- Le login existe deja : {} --", request.getLogin());
//            return response.error("Le login existe deja : ", request.getLogin());
//        }

        if (userRepository.emailNotUpdatable(user.getId(), request.getEmail())) {
            log.error("-- L'email existe deja : {} --", request.getEmail());
            return response.error("L'email existe deja : ", request.getEmail());
        }

        if (userRepository.phoneNumberNotUpdatable(user.getId(), request.getPhoneNumber())) {
            log.error("-- Le numero de telephone existe deja : {} --", request.getPhoneNumber());
            return response.error("Le numero de telephone existe deja : ", request.getPhoneNumber());
        }

//        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.uAudit();
        User userSaved = userRepository.saveAndFlush(user);
        log.info("-- Fin modification --");
        return response.success(mapper.toDto(userSaved));
    }

}
