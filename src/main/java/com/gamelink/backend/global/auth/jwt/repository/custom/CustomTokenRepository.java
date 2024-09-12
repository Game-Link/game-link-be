package com.gamelink.backend.global.auth.jwt.repository.custom;

import com.gamelink.backend.global.auth.model.JwtToken;

public interface CustomTokenRepository {
    void updateJwtToken(String userSubId, String accessToken, String refreshToken);

    String findAccessToken(String refreshToken);
}
