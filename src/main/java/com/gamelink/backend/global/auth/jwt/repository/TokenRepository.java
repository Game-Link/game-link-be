package com.gamelink.backend.global.auth.jwt.repository;

import com.gamelink.backend.global.auth.jwt.repository.custom.CustomTokenRepository;
import com.gamelink.backend.global.auth.model.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<JwtToken, String>, CustomTokenRepository {
}
