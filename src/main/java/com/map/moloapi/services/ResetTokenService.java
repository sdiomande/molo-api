package com.map.moloapi.services;

import com.map.moloapi.contracts.ExpiryToken;
import com.map.moloapi.contracts.Response;
import com.map.moloapi.entities.PasswordResetToken;
import com.map.moloapi.entities.User;
import com.map.moloapi.repositories.PasswordResetTokenRepository;
import com.map.moloapi.utils.Utilities;
import com.map.moloapi.utils.constants.FunctionalMessage;
import com.map.moloapi.utils.constants.TechnicalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author DIOMANDE Souleymane
 * @Date 27/04/2023 06:32
 */
@Service
@Slf4j
public class ResetTokenService {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    Utilities utilities;

    public PasswordResetToken buildResetUrl(User user){
        log.info("-- START BUILD RESET URL, USER: {} --", user.getEmail());
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .url(utilities.getParam("RESET_PASSWORD_URL").concat("/").concat(token))
                .user(user)
                .expiryDateTimestamp(Utilities.timestamp() + (15 * 60 * 1000))
//                .expiryDate()
                .build();
//        passwordResetToken.setExpiryDate(Integer.parseInt(utilities.getParam(ParamEnum.TTL.getValue())));
        passwordResetToken.setExpiryDateTimestamp(passwordResetToken.getExpiryDate().getTime());
        PasswordResetToken passwordResetTokenSaved = passwordResetTokenRepository.saveAndFlush(passwordResetToken);
        log.info("-- END BUILD RESET URL --");
        return passwordResetTokenSaved;
    }

    public String getResetUrl(User user){
        log.info("-- START BUILD RESET URL, USER: {} --", user.getEmail());
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .url(utilities.getParam("RESET_PASSWORD_URL").concat("/").concat(token))
                .user(user)
                .build();
        passwordResetToken.setExpiryDate(Integer.parseInt(utilities.getParam("TTL")));
        passwordResetToken.setExpiryDateTimestamp(passwordResetToken.getExpiryDate().getTime());
        passwordResetTokenRepository.saveAndFlush(passwordResetToken);
        log.info("-- END BUILD RESET URL --");
        return passwordResetToken.getUrl();
//        return passwordResetTokenSaved;
    }

    public ExpiryToken tokenHasExpiry(User user){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findActiveTokenByUserId(user.getId(), Utilities.timestamp());

        if (passwordResetToken == null)
            return ExpiryToken.builder()
                    .expiry(true)
                    .build();

        return (passwordResetToken.isExpired()) ?
                ExpiryToken.builder()
                        .expiry(true)
                        .build() :
                ExpiryToken.builder()
                        .expiry(false)
                        .message("Un mail de reinitilisation de mot de passe a deja ete envoye a l'utilisateur : "+ user.getEmail() +
                                ", veuillez attendre "+ Utilities.minutes(passwordResetToken.getExpiryDateTimestamp()) +" minute(s) avant de faire une nouvelle demande.")
                        .build();
    }

    public Response verifyToken(String token){
        log.info("-- Debut verification du token (reinitialisation mot de passe), {} --", token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.verifyToken(token, Utilities.timestamp());
        if (passwordResetToken != null){
            log.info("-- Fin verification du token --");
            return Response.builder()
                    .hasError(false)
                    .message(TechnicalMessage.SUCCESS_MESSAGE)
                    .build();
        }
        log.info("-- Fin verification du token, token expiré/inexistant --");
        return Response.builder()
                .hasError(true)
                .message(FunctionalMessage.TOKEN_EXPIRY)
                .build();
    }

    public User verifyTokenUser(String token){
        log.info("-- Debut verification du token utilisateur, {} --", token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.verifyToken(token, Utilities.timestamp());
        if (passwordResetToken != null){
            log.info("-- Fin verification du token --");
            return passwordResetToken.getUser();
        }
        log.info("-- Fin verification du token, aucun utilisateur trouve --");
        return null;
    }

    public void forceTokenExpiry(String token){
        log.info("-- START FORCE EXPIRY TOKEN, {} --", token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken != null){
            log.info("-- END FORCE EXPIRY TOKEN, {} --", token);
            passwordResetToken.setToken("EXP###".concat(token));
            passwordResetTokenRepository.saveAndFlush(passwordResetToken);
        }
    }
}
