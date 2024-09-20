package com.gamelink.backend.global.auth.jwt.repository.custom.impl;

import com.gamelink.backend.global.auth.jwt.exception.InvalidTokenException;
import com.gamelink.backend.global.auth.jwt.repository.custom.CustomTokenRepository;
import com.gamelink.backend.global.auth.model.JwtToken;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomTokenRepositoryImpl implements CustomTokenRepository {

    private final MongoTemplate mongoTemplate;

    /**
     * userSubId로 JwtToken의 accessToken, refreshToken 업데이트
     */
    @Override
    public void updateJwtToken(String userSubId, String accessToken, String refreshToken) {
        if (userSubId == null || accessToken == null || refreshToken == null) {
            throw new IllegalArgumentException();
        }

        ZonedDateTime seoulTime = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
        Instant updatedAt = seoulTime.toInstant();

        UpdateResult result = mongoTemplate.updateFirst(
                new Query(Criteria.where("userSubId").is(userSubId)),
                new Update().set("accessToken", accessToken)
                        .set("refreshToken", refreshToken)
                        .set("updatedAt", updatedAt),
                JwtToken.class
        );
    }

    @Override
    public String findAccessToken(String refreshToken) {
        return mongoTemplate.find(
                        new Query(Criteria.where("refreshToken").is(refreshToken)), JwtToken.class)
                .stream()
                .findFirst()
                .orElseThrow(InvalidTokenException::new)
                .getAccessToken();
    }

    @Override
    public Optional<JwtToken> findUserToken(String userSubId) {
        return mongoTemplate.find(
                        new Query(Criteria.where("userSubId").is(userSubId)), JwtToken.class)
                .stream().findFirst();
    }
}
