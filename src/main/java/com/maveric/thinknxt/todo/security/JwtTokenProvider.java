package com.maveric.thinknxt.todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime()+jwtExpirationDate);
        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // Get Username from token
    public String getUsername(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parse(token);
        } catch (Exception e) {
            throw new Exception("Could not verify JWT token integrity!", e);
        }
        return true;
    }
}
