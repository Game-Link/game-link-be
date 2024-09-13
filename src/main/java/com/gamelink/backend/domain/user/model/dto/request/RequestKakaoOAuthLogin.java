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

    @Schema(description = "OAuth 토큰", example = "eofnadwkvjabweiubewubwdijcbeij")
    private String authToken;

    @Schema(description = "사용자 기기 고유 Id", example = "4joirnoifnvaorv")
    private String uniqueId;

    @Schema(description = "디바이스 Id")
    private String deviceId;

    @Schema(description = "디바이스 이름")
    private String deviceName;

    @Schema(description = "모델명")
    private String model;


}
