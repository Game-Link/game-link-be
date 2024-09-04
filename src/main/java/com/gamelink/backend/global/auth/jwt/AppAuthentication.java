package com.gamelink.backend.global.auth.jwt;

import com.gamelink.backend.domain.user.model.UserStatus;
import com.gamelink.backend.global.auth.UserRole;
import org.springframework.security.core.Authentication;

public interface AppAuthentication extends Authentication {
    String getUserSubId();

    UserRole getUserRole();

    UserStatus getUserStatus();

    boolean isAdmin();
}
