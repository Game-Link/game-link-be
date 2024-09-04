package com.gamelink.backend.domain.user.model.entity;

import com.gamelink.backend.domain.user.model.UserStatus;
import com.gamelink.backend.global.auth.UserRole;
import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    private UserRole userRole;
    private UserStatus status;

    @Builder
    private User(UserRole userRole, UserStatus status) {
        this.userRole = userRole;
        this.status = status;
    }
}
