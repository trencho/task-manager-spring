package com.project.taskmanager.service;

import java.util.Optional;

import com.project.taskmanager.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String username);

    String refreshAccessToken(String refreshToken);

    Optional<RefreshToken> findByToken(String token);

    boolean isTokenValid(RefreshToken refreshToken);

    void deleteByToken(String token);

    void deleteByUsername(String userId);

    Optional<RefreshToken> verifyExpiration(RefreshToken token);

}
