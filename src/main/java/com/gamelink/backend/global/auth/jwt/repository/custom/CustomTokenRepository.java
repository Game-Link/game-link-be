package com.gamelink.backend.global.auth.jwt.repository.custom;

import com.gamelink.backend.global.auth.model.JwtToken;

import java.util.Optional;
import java.util.UUID;

public interface CustomTokenRepository {
    void updateJwtToken(String userSubId, String accessToken, String refreshToken);

    String findAccessToken(String refreshToken);

    Optional<JwtToken> findUserToken(UUID subId);
}
