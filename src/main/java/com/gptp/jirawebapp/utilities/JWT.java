package com.gptp.jirawebapp.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JWT {
    private final SecretKey key;

    public JWT(@Value("${app.jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(JWTContent content) throws Exception {
        return Jwts.builder().claim("userId", content.userId).signWith(key).compact();
    }

    public JWTContent decode(String token) throws Exception {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        String userId = claims.get("userId", String.class);
        return new JWTContent(userId);
    }
}

