package com.gamelink.backend.domain.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestKakaoOAuthLogin {

    @Schema(description = "카카오 Access Token")
    private String accessToken;

    @Schema(description = "사용자 고유 기기 ID")
    private String deviceId;
}
