package com.project.taskmanager.security;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.taskmanager.entity.RefreshToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(final String username) {
        final var now = new Date();
        final var validity = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public RefreshToken generateRefreshToken(final String username) {
        final var now = new Date();
        final var expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        final var token = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        return RefreshToken.builder()
                .token(token)
                .username(username)
                .expiryDate(expiryDate.toInstant())
                .build();
    }

    public String getUsername(final String token) {
        return Jwts.parser().setSigningKey(jwtSecret).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public String resolveToken(final HttpServletRequest request) {
        final var bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
