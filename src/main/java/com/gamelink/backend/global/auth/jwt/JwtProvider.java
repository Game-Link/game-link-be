package com.gamelink.backend.global.auth.jwt;

import com.gamelink.backend.domain.user.model.UserStatus;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.auth.UserRole;
import com.gamelink.backend.global.auth.jwt.exception.IllegalTokenException;
import com.gamelink.backend.global.auth.jwt.repository.TokenRepository;
import com.gamelink.backend.global.auth.model.JwtToken;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider implements AuthenticationTokenProvider {

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_PREFIX = "gamelink-access";
    public static final String REFRESH_PREFIX = "gamelink-refresh";

    @Value("${app.auth.jwt.access-expiration}")
    private Duration accessExpiration;

    @Value("${app.auth.jwt.refresh-expiration}")
    private Duration refreshExpiration;

    @Value("${app.auth.jwt.secret-key}")
    private String secretKey;

    private final TokenRepository tokenRepository;

    @Override
    public String getTokenFromHeader(HttpServletRequest request, String prefix) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        Optional<Cookie> maybeCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase(prefix))
                .findFirst();

        if (maybeCookie.isPresent()) {
            return maybeCookie.get().getValue();
        }

        String header = request.getHeader(AUTHORIZATION);
        if (header != null) {
            if (!header.toLowerCase().startsWith("bearer ")) {
                throw new IllegalTokenException("token doesn't start with bearer");
            }

            return header.substring(7);
        }

        return null;
    }

    @Override
    public JwtAuthentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getTokenFromHeader(request, ACCESS_PREFIX);
        if (accessToken == null) {
            throw new IllegalTokenException("token is null");
        }
        Jws<Claims> claimsJws;
        try {
            claimsJws = validateToken(accessToken);
            return createJwtAuthentication(claimsJws);
        } catch (ExpiredJwtException e) {
            // AccessToken이 만료된 경우, RefreshToken을 이용하여 재발급
            JwtToken jwtToken = tokenRepository.findRefreshToken(accessToken);
            String refreshToken = jwtToken.getRefreshToken();
            if (!validateRefreshToken(refreshToken)) {
                tokenRepository.deleteByUserSubId(jwtToken.getUserSubId());
                throw new IllegalTokenException("refresh token is invalid");
            }
            AuthenticationToken newToken = reissue(accessToken);
            claimsJws = validateToken(newToken.getAccessToken());
            response.addHeader(HttpHeaders.SET_COOKIE, ACCESS_PREFIX + "=" + newToken.getAccessToken());
            response.addHeader(HttpHeaders.SET_COOKIE, REFRESH_PREFIX + "=" + newToken.getRefreshToken());
            tokenRepository.updateJwtToken(claimsJws.getBody().get("userSubId", String.class), newToken.getAccessToken(), newToken.getRefreshToken());
            return createJwtAuthentication(claimsJws);
        } catch (JwtException e) {
            throw new IllegalTokenException("token is invalid");
        }
    }

    public AuthenticationToken issue(User user) {
        JwtAuthenticationToken token = JwtAuthenticationToken.builder()
                .accessToken(createAccessToken(user.getSubId().toString(), user.getUserRole(), user.getStatus()))
                .refreshToken(createRefreshToken())
                .build();
        tokenRepository.save(new JwtToken(user.getSubId().toString(), token.getAccessToken(), token.getRefreshToken(), LocalDateTime.now(), LocalDateTime.now()));
        return token;
    }

    private JwtAuthenticationToken reissue(String accessToken) {
        return JwtAuthenticationToken.builder()
                .accessToken(refreshAccessToken(accessToken))
                .refreshToken(createRefreshToken())
                .build();
    }

    private String refreshAccessToken(String accessToken) {
        String userSubId;
        UserRole role;
        UserStatus userStatus;
        try {
            Jws<Claims> claimsJws = validateToken(accessToken);
            Claims body = claimsJws.getBody();
            userSubId = (String) body.get("userSubId");
            role = UserRole.of((String) body.get("userRole"));
            userStatus = UserStatus.valueOf((String) body.get("userStatus"));
        } catch (ExpiredJwtException e) {
            userSubId = (String) e.getClaims().get("userSubId");
            role = UserRole.of((String) e.getClaims().get("userRole"));
            userStatus = UserStatus.valueOf((String) e.getClaims( ).get("userStatus"));
        }
        return createAccessToken(userSubId, role, userStatus);
    }

    private String createAccessToken(String userSubId, UserRole role, UserStatus status) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(accessExpiration);

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("userSubId", userSubId);
        payloads.put("userRole", role);
        payloads.put("userStatus", status);

        return Jwts.builder()
                .setSubject("UserInfo")
                .setClaims(payloads)
                .setIssuedAt(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    private String createRefreshToken() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plus(refreshExpiration);
        return Jwts.builder()
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    private Jws<Claims> validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.error("token is expired");
            throw e;
        } catch (JwtException e) {
            log.error("token is invalid");
            throw e;
        }
    }

    private boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("refresh token is expired");
            return false;
        } catch (JwtException e) {
            log.error("refresh token is invalid");
            return false;
        }
    }

    private JwtAuthentication createJwtAuthentication(Jws<Claims> claimsJws) {
        Claims body = claimsJws.getBody();
        String userSubId = (String) body.get("userSubId");
        UserRole userRole = UserRole.valueOf((String) body.get("userRole"));
        UserStatus userStatus = UserStatus.valueOf((String) body.get("userStatus"));

        return new JwtAuthentication(userSubId, userRole, userStatus);
    }
}
