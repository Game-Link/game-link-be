package com.gamelink.backend.global.auth.jwt.repository.custom;

import com.gamelink.backend.global.auth.model.JwtToken;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomTokenRepository {
    void updateJwtToken(@Param("userSubId") String userSubId, @Param("accessToken") String accessToken, @Param("refreshToken") String refreshToken);

    String findAccessToken(@Param("refreshToken") String refreshToken);

    Optional<JwtToken> findUserToken(@Param("userSubId") String userSubId);
}
