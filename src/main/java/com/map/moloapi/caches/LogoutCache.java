package com.map.moloapi.caches;

import com.map.moloapi.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author DIOMANDE Souleymane
 * @Date 28/04/2023 10:13
 */
@Component
@Slf4j
public class LogoutCache {

    @Value("${jwt-secret}")
    private String jwtSecret;

    private ExpiringMap<String, User> expiringMap;

    public LogoutCache() {
        this.expiringMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(1000)
                .build();
    }

    public void markLogoutEventForToken(User user, String token) {
        if (!expiringMap.containsKey(token)) {
            Date tokenExpiryDate = getExpirationTimeToToken(token);
            long ttlForToken = getTTLForToken(tokenExpiryDate);
            expiringMap.put(token, user, ttlForToken, TimeUnit.SECONDS);
        }
    }

    public User getLogoutEventForToken(String token) {
        return expiringMap.get(token);
    }

    private long getTTLForToken(Date date) {
        long secondAtExpiry = date.toInstant().getEpochSecond();
        long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }

    public Date getExpirationTimeToToken(String token) {
        Jws<Claims> body = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        return body.getBody().getExpiration();
    }

}
