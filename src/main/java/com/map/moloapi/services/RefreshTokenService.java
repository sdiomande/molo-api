package com.map.moloapi.services;

import com.map.moloapi.entities.RefreshToken;
import com.map.moloapi.repositories.RefreshTokenRepository;
import com.map.moloapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * @author DIOMANDE Souleymane
 * @Date 13/01/2023 17:38
 */
@Service
@Slf4j
public class RefreshTokenService {
    @Value("${jwt-refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public void deleteRefreshToken(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            log.error( "Token de rafraichissement expire, veuillez vous reconnecter. Token : {}", token.getToken());
            throw new RuntimeException("Impossible de continuer l'operation");
        }
        return token;
    }

    public void deleteExpiryTokens(){
        log.info("-- Debut suppression tokens expirés --");
        refreshTokenRepository.findAll().forEach(refreshToken -> {
            if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0){
                refreshTokenRepository.delete(refreshToken);
            }
        });
        log.info("-- Fin suppression tokens expirés --");
    }
}
