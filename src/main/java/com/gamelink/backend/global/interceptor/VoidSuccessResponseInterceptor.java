package com.gamelink.backend.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.global.auth.jwt.JwtAuthentication;
import com.gamelink.backend.global.auth.jwt.JwtProvider;
import com.gamelink.backend.global.error.exception.AccessTokenRequiredException;
import com.gamelink.backend.global.model.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class VoidSuccessResponseInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            boolean isNotSecured = (boolean) request.getAttribute("access-token-none");

            if (handlerMethod == null || handlerMethod.getMethod().isAnnotationPresent(Secured.class)) {
                if (isNotSecured) {
                    throw new AccessTokenRequiredException();
                } else {
                    JwtAuthentication authentication = jwtProvider.getAuthentication(request, response);
                    if (!authentication.getUserStatus().isActive()) {
                        throw new UserNotFoundException();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex) throws Exception {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (!status.is2xxSuccessful()) {
            return;
        }

        if (response.getContentType() != null) {
            return;
        }

        String wrappedBody = objectMapper.writeValueAsString(new SuccessResponse());

        byte[] bytesData = wrappedBody.getBytes();
        response.setContentType("application/json");
        response.resetBuffer();
        response.getOutputStream().write(bytesData, 0, bytesData.length);
    }
}
