package com.gamelink.backend.domain.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.gamelink.backend.domain.user.exception.DeviceNotFoundException;
import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.Enrolled;
import com.gamelink.backend.domain.user.model.dto.request.RequestKakaoOAuthLogin;
import com.gamelink.backend.domain.user.model.dto.response.ResponseOAuthLoginDto;
import com.gamelink.backend.domain.user.model.entity.Device;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.DeviceRepository;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.domain.user.service.OAuthService;
import com.gamelink.backend.global.auth.jwt.AuthenticationToken;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public ResponseOAuthLoginDto kakaoLogin(RequestKakaoOAuthLogin request) {
        // TODO : OAuth (Kakao, Naver, Google) 로그인 토큰 관리 필요
        JsonNode userResource = getUserResource(request.getKakaoInfo().getAccess_token(), "kakao");

        String name = userResource.get("kakao_account").get("name").asText();
        String email = userResource.get("kakao_account").get("email").asText();
        String phone = userResource.get("kakao_account").get("phone_number").asText();

        Optional<User> maybeUser = userRepository.findByName(name);
        User user;

        if (maybeUser.isPresent()) {
            user = maybeUser.get();
        } else {
            user = User.builder()
                    .email(email)
                    .phone(phone)
                    .nickname("testNickname")
                    .name(name)
                    .enrolled(Enrolled.KAKAO)
                    .build();
            user = userRepository.save(user);
            Device device = Device.convertFromLoginRequest(user, request.getDeviceInfo());
            deviceRepository.save(device);
        }
        AuthenticationToken newToken = jwtProvider.issue(user);
        Device device = deviceRepository.findOneByUserSubId(user.getSubId()).orElseThrow(DeviceNotFoundException::new);
        return new ResponseOAuthLoginDto(device, newToken);
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        String resourceUri = env.getProperty("app." + registrationId + ".resource-uri");
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
