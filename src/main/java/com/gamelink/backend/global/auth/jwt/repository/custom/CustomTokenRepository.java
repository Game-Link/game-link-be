package com.gamelink.backend.global.auth.jwt.repository.custom;

import com.gamelink.backend.global.auth.model.JwtToken;

public interface CustomTokenRepository {
    JwtToken findRefreshToken(String accessToken);

    void updateJwtToken(String userSubId, String accessToken, String refreshToken);

    void deleteByUserSubId(String userSubId);
}
