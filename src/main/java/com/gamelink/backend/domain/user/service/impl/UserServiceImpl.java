package com.gamelink.backend.domain.user.service.impl;

import com.gamelink.backend.domain.user.model.dto.response.ResponseToken;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.domain.user.service.UserService;
import com.gamelink.backend.global.auth.jwt.JwtAuthenticationToken;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.auth.jwt.exception.InvalidTokenException;
import com.gamelink.backend.global.auth.jwt.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public ResponseToken reissue(String refreshToken) {
        if(!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException();
        }
        String accessToken = tokenRepository.findAccessToken(refreshToken);
        JwtAuthenticationToken newToken = jwtProvider.reissue(accessToken);
        return new ResponseToken(newToken);
    }
}
