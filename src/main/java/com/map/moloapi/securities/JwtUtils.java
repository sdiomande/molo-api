package com.map.moloapi.securities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.moloapi.caches.LogoutCache;
import com.map.moloapi.contracts.users.responses.AuthResponse;
import com.map.moloapi.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author DIOMANDE Souleymane
 * @Date 16/10/2022 18:42
 */
@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt-secret}")
    private String jwtSecret;

    @Value("${jwt-expiration-ms}")
    private int jwtExpirationMs;

    @Autowired
    LogoutCache logoutCache;

    ObjectMapper objectMapper = new ObjectMapper();
    DefaultJwtParserBuilder defaultJwtParserBuilder = new DefaultJwtParserBuilder();
    public String generateJwtToken(Authentication authentication, AuthResponse authResponse) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setClaims(objectMapper.convertValue(authResponse, Map.class))
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Map<String, Object> map, String username) {
        return Jwts.builder()
                .setClaims(map)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

//    public String generateJwtToken(User user) {
//        AuthResponse authResponse = AuthResponse.builder().build();
//        return generateJwtToken(Utilities.authenticationConnected(), authResponse);
//    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserNameFromJwtTokenx(String token) {
        Jws<Claims> body = defaultJwtParserBuilder.setSigningKey(jwtSecret).build().parseClaimsJws(token);
        return body.getBody().getSubject();
    }

    public AuthResponse getAuthResponseFromToken(String token) {
        Jws<Claims> body = defaultJwtParserBuilder.setSigningKey(jwtSecret).build().parseClaimsJws(token);
        try {
            String json = objectMapper.writeValueAsString(body.getBody());
            return objectMapper.readValue(json, AuthResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            defaultJwtParserBuilder.setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            validateTokenIsNotForALoggedOutDevice(authToken);
            return true;
        }
        catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
//            throw new RuntimeException("Invalid JWT signature: \n" + e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
//            throw new RuntimeException("Invalid JWT token: \n" + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
//            throw new RuntimeException("JWT token is expired: \n" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
//            throw new RuntimeException("JWT token is unsupported: \n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
//            throw new RuntimeException("JWT claims string is empty: \n" + e.getMessage());
        }
        return false;
    }


    private void validateTokenIsNotForALoggedOutDevice(String authToken) {
        User user = logoutCache.getLogoutEventForToken(authToken);
        if (user != null) {log.error("Token expiré, veuillez vous reconnecter.");
            throw new RuntimeException("Token expiré, veuillez vous reconnecter.");
        }
    }
}
