package com.gamelink.backend.domain.user.model.dto.response;

import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.auth.jwt.AuthenticationToken;
import lombok.Getter;

@Getter
public class ResponseOAuthLoginDto {

    private final String accessToken;

    private final String refreshToken;

    private final String uniqueId;

    public ResponseOAuthLoginDto(User user, AuthenticationToken token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.uniqueId = "testUniqueId";
    }
}
