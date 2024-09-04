package com.gamelink.backend.global.auth;

import com.gamelink.backend.global.auth.jwt.AuthenticationTokenProvider;
import com.gamelink.backend.global.auth.jwt.GuestAuthentication;
import com.gamelink.backend.global.auth.jwt.JwtAuthentication;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.auth.jwt.exception.RefreshTokenNotFoundException;
import com.gamelink.backend.global.auth.jwt.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationTokenProvider authenticationTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String accessToken = authenticationTokenProvider.getTokenFromHeader(request, JwtProvider.ACCESS_PREFIX);
        String refreshToken = authenticationTokenProvider.getTokenFromHeader(request, JwtProvider.REFRESH_PREFIX);
        if (accessToken == null) {
            request.setAttribute("access-token-none", true);
            SecurityContextHolder.getContext().setAuthentication(GuestAuthentication.getAuthentication());
        } else if (refreshToken == null) {
            throw new RefreshTokenNotFoundException();
        } else {
            JwtAuthentication authentication = authenticationTokenProvider.getAuthentication(request, response);
            request.setAttribute("access-token-none", false);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();

    }
}
