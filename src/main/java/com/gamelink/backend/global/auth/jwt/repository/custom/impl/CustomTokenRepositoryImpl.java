package com.gamelink.backend.global.auth.jwt.repository.custom.impl;

import com.gamelink.backend.global.auth.jwt.exception.InvalidTokenException;
import com.gamelink.backend.global.auth.jwt.repository.custom.CustomTokenRepository;
import com.gamelink.backend.global.auth.model.JwtToken;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomTokenRepositoryImpl implements CustomTokenRepository {

    private final MongoTemplate mongoTemplate;

    /**
     * userSubId로 JwtToken의 accessToken, refreshToken 업데이트
     */
    @Override
    public void updateJwtToken(String userSubId, String accessToken, String refreshToken) {
        try {
            Query query = new Query(Criteria.where("userSubId").is(userSubId));

            Update update = new Update();
            update.set("accessToken", accessToken);
            update.set("refreshToken", refreshToken);
            update.set("updatedAt", LocalDateTime.now());

            UpdateResult result = mongoTemplate.updateFirst(query, update, JwtToken.class);
            if (result.getMatchedCount() == 0) {
                throw new NullPointerException("토큰 객체를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 로그로 출력
            throw new RuntimeException("Failed to update JwtToken", e);
        }
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
