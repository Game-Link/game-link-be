package com.gamelink.backend.domain.user.model;

public enum UserStatus {
    ACTIVE,
    INACTIVE;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isStatus(UserStatus status) {
        return this == status;
    }
}
