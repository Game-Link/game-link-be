package com.gamelink.backend.domain.user.service;

import com.gamelink.backend.domain.user.model.dto.request.RequestKakaoOAuthLogin;
import com.gamelink.backend.domain.user.model.dto.response.ResponseOAuthLoginDto;

public interface OAuthService {
    ResponseOAuthLoginDto kakaoLogin(RequestKakaoOAuthLogin request);
}
