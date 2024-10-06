package com.gamelink.backend.debug.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamelink.backend.domain.user.model.Enrolled;
import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.global.auth.jwt.AuthenticationToken;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.auth.jwt.repository.TokenRepository;
import com.gamelink.backend.global.auth.model.JwtToken;
import com.gamelink.backend.global.auth.role.UserAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@ConditionalOnExpression("${app.enable-test-controller:true}")
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class TestController {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 테스트 로그인
     */
    @PostMapping("/issue")
    @Transactional
    public ResponseToken issueToken() {
        Optional<User> maybeUser = userRepository.findByName("테스트");
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            AuthenticationToken token = jwtProvider.issue(user);
            tokenRepository.save(new JwtToken(user.getSubId().toString(), token.getAccessToken(), token.getRefreshToken(), LocalDateTime.now(), LocalDateTime.now()));
            return new ResponseToken(token);
        } else {
            User user = User.builder()
                    .name("테스트")
                    .email("example@naver.com")
                    .phone("01011112222")
                    .enrolled(Enrolled.NAVER)
                    .build();
            userRepository.save(user);
            AuthenticationToken token = jwtProvider.issue(user);
            tokenRepository.save(new JwtToken(user.getSubId().toString(), token.getAccessToken(), token.getRefreshToken(), LocalDateTime.now(), LocalDateTime.now()));
            return new ResponseToken(token);
        }
    }

    /**
     * 유저 토큰 확인
     */
    @GetMapping
    @UserAuth
    public String test() {
        return "테스트 완료";
    }

    /**
     * 카프카 메시지 전송 테스트
     */
    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestParam String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String stringChat = objectMapper.writeValueAsString(message);
            log.info("MessageSenderImpl Message -> String형 : " + stringChat);
            kafkaTemplate.send("chatting", stringChat);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @KafkaListener(topics = "chatting", containerFactory = "kafkaConsumerContainerFactory")
    public void receiveMessage(String stringChat) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        Message message = objectMapper.readValue(stringChat, Message.class);
        log.info("MessageSenderImpl Message -> STring형: " + stringChat);
    }

    /**
     * 카카오 AccessToken 발급
     */
    @PostMapping("/kakao")
    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=edbb963a11dccbcf54be5f9d18af4636&redirect_uri=http://localhost:8080/test/oauth2/code/kakao&response_type=code&scope=account_email,name,phone_number";
    }

    /**
     * 카카오 AccessToken 발급
     **/
    @GetMapping("/oauth2/code/{registrationId}")
    public String testLogin(@RequestParam String code, @PathVariable String registrationId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String clientId = "edbb963a11dccbcf54be5f9d18af4636";
        String redirectUrl = "http://localhost:8080/test/oauth2/code/kakao";
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        params.add("code", code);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        String accessToken = response.getBody().get("access_token").asText();
        return "accessToken : " + accessToken;
    }

    /**
     * ArgoCD rollout 확인
     */
    @GetMapping("/argocd")
    public String testString() {
        return "test";
    }

}
