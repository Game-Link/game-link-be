package com.gamelink.backend.domain.user.controller;

import com.gamelink.backend.domain.user.model.dto.request.RequestReissue;
import com.gamelink.backend.domain.user.model.dto.response.ResponseOAuthLoginDto;
import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;
import com.gamelink.backend.domain.user.service.OAuthService;
import com.gamelink.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "유저", description = "유저 관련 API")
@Slf4j
public class UserController {

    private final UserService userService;
    private final OAuthService oAuthService;

    /**
     * OAuth 로그인
     *
     * <p>AuthToken은 OAuth 로그인 후 생성된 사용자 고유 토큰 값입니다.</p>
     * <p>고유 토큰으로 API를 호출 시 회원가입 또는 로그인이 진행되어 URL에 access, refresh 토큰을 담아서 전달합니다.</p>
     */
    @GetMapping("/oauth/callback")
    public RedirectView redirectKakaoOAuth(@RequestParam(value = "code") String authToken) {
        ResponseOAuthLoginDto result = oAuthService.kakaoLogin(authToken);
        log.info("토큰 발급 성공!");
        String redirectUrl = "kakao01302532fd3136475b7951652fe58666://oauth" +
                "?accessToken=" + result.getAccessToken() + "&refreshToken=" + result.getRefreshToken() +
                "uniqueId=" + result.getUniqueId();

        return new RedirectView(redirectUrl);
    }


    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseToken reissueToken(@RequestBody RequestReissue request) {
        return userService.reissue(request.getRefreshToken());
    }
}
