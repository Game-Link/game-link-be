package com.gamelink.backend.domain.user.service;

import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;

public interface UserService {
    ResponseToken reissue(String refreshToken);
}
