package org.example.eventmanager.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final long expire;
    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.expire}") long expire, @Value("${jwt.key}") String key) {
        this.expire = expire;
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateJwt(String login, Long id, String role) {
        return Jwts.builder()
                .subject(login)
                .claim("id", id)
                .claim("role", role)
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expire))
                .signWith(secretKey)
                .compact();
    }

    public String getLoginFromJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
