package com.gamelink.backend.global.auth.jwt.repository.custom.impl;

import com.gamelink.backend.global.auth.jwt.exception.InvalidTokenException;
import com.gamelink.backend.global.auth.jwt.exception.JwtTokenNotFoundException;
import com.gamelink.backend.global.auth.jwt.repository.custom.CustomTokenRepository;
import com.gamelink.backend.global.auth.model.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomTokenRepositoryImpl implements CustomTokenRepository {

    private final MongoTemplate mongoTemplate;

    /**
     * userSubId로 JwtToken의 accessToken, refreshToken 업데이트
     */
    @Override
    public void updateJwtToken(String userSubId, String accessToken, String refreshToken) {
        Query query = new Query(Criteria.where("userSubId").is(userSubId));

        Update update = new Update();
        update.set("accessToken", accessToken);
        update.set("refreshToken", refreshToken);
        update.set("updatedAt", System.currentTimeMillis());

        mongoTemplate.updateFirst(query, update, JwtToken.class);
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
        JwtToken maybeJwtToken = mongoTemplate.findOne(
                new Query(Criteria.where("userSubId").is(userSubId)), JwtToken.class);
        if (maybeJwtToken == null) {
            throw new JwtTokenNotFoundException();
        } else {
            return Optional.of(maybeJwtToken);
        }
    }
}
