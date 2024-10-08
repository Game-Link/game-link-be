package com.gamelink.backend.domain.user.service;

import com.gamelink.backend.domain.user.model.dto.request.RequestCreateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.request.RequestUpdateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;

import java.util.UUID;

public interface UserService {
    ResponseToken reissue(String refreshToken);

    void createUserProfileImage(RequestCreateUserProfileImage request, UUID userSubId);

    void updateUserProfileImage(RequestUpdateUserProfileImage request, UUID userSubId);
}
