package com.gamelink.backend.domain.user.model.dto.response;

import com.gamelink.backend.domain.user.model.entity.Device;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.auth.jwt.AuthenticationToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseOAuthLoginDto {

    @Schema(description = "액세스 토큰")
    private final String accessToken;

    @Schema(description = "리프레시 토큰")
    private final String refreshToken;

    @Schema(description = "사용자 기기 Id")
    private final String uniqueId;

    public ResponseOAuthLoginDto(Device device, AuthenticationToken token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.uniqueId = device.getUniqueId();
    }
}
