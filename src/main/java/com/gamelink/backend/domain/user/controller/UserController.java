package com.gamelink.backend.domain.user.controller;

import com.gamelink.backend.domain.user.model.dto.request.RequestCreateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.request.RequestKakaoOAuthLogin;
import com.gamelink.backend.domain.user.model.dto.request.RequestReissue;
import com.gamelink.backend.domain.user.model.dto.request.RequestUpdateUserProfileImage;
import com.gamelink.backend.domain.user.model.dto.response.ResponseOAuthLoginDto;
import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;
import com.gamelink.backend.domain.user.service.OAuthService;
import com.gamelink.backend.domain.user.service.UserService;
import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import com.gamelink.backend.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "유저", description = "유저 관련 API")
@Slf4j
public class UserController {

    private final UserService userService;
    private final OAuthService oAuthService;

//    /**
//     * 사용자 프로필 조회
//     */
//    @GetMapping("/profile/{userId}")
//    @UserAuth
//    public ResponseUserProfileDto getUserProfile(@PathVariable UUID userId) {
//        return userService.getUserProfile(userId);
//    }

    /**
     * 사용자 프로필 이미지 등록
     */
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public void createUserProfile(@ModelAttribute RequestCreateUserProfileImage request,
                                          AppAuthentication auth) {
        userService.createUserProfileImage(request, UUID.fromString(auth.getUserSubId()));
    }

    /**
     * 사용자 프로필 이미지 수정
     */
    @PatchMapping(value = "/profile/change", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public void updateUserProfile(@ModelAttribute RequestUpdateUserProfileImage request,
                                  AppAuthentication auth) {
        userService.updateUserProfileImage(request, UUID.fromString(auth.getUserSubId()));
    }

    /**
     * OAuth 로그인
     *
     * <p>AuthToken은 OAuth 로그인 후 생성된 사용자 고유 토큰 값입니다.</p>
     * <p>고유 토큰으로 API를 호출 시 회원가입 또는 로그인이 진행되어 URL에 access, refresh 토큰을 담아서 전달합니다.</p>
     */
    @PostMapping("/oauth/kakao/login")
    public ResponseOAuthLoginDto redirectKakaoOAuth(@RequestBody RequestKakaoOAuthLogin request) {
        return oAuthService.kakaoLogin(request);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseToken reissueToken(@RequestBody RequestReissue request) {
        return userService.reissue(request.getRefreshToken());
    }
}
