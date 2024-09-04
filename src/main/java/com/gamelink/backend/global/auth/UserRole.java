package com.gamelink.backend.global.auth;

import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gamelink.backend.global.auth.UserAuthNames.*;

@Getter
public enum UserRole {
    GUEST(ROLE_GUEST),
    USER(ROLE_USER),
    ADMIN(combine(ROLE_ADMIN, ROLE_USER, ROLE_GUEST));

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static final Map<String, UserRole> BY_LABEL =
            Stream.of(values()).collect(Collectors.toMap(UserRole::getName, e -> e));

    public static UserRole of(String name) {
        return BY_LABEL.get(name);
    }

    public boolean contains(String role) {
        return Arrays.stream(this.getName().split(","))
                .anyMatch(avail -> avail.equals(role));
    }

    public static UserRole from(AppAuthentication auth) {
        if (auth == null) {
            return GUEST;
        }
        return auth.getUserRole();
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isUser() {
        return this == USER;
    }
}
