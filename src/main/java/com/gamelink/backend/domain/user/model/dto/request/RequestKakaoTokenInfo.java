package com.gamelink.backend.domain.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestKakaoTokenInfo {

    private String access_token;

    private int expires_in;

    private String refresh_token;

    private int refresh_token_expires_in;

    private String scope;

    private String token_type;
}
