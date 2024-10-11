package com.gamelink.backend.domain.user.model.dto.response;

import com.gamelink.backend.domain.user.model.entity.Device;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.auth.jwt.AuthenticationToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ResponseOAuthLoginDto {

    @Schema(description = "유저 Id")
    private final UUID userId;

    @Schema(description = "액세스 토큰")
    private final String accessToken;

    @Schema(description = "리프레시 토큰")
    private final String refreshToken;

    @Schema(description = "사용자 기기 Id")
    private final String uniqueId;

    public ResponseOAuthLoginDto(UUID userId, Device device, AuthenticationToken token) {
        this.userId = userId;
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.uniqueId = device.getUniqueId();
    }
}
