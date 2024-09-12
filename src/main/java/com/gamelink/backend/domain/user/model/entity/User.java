package com.gamelink.backend.domain.user.model.entity;

import com.gamelink.backend.domain.user.model.Enrolled;
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

    private String name;
    private String nickname;
    private String email;
    private String phone;
    private Enrolled enrolled;
    private UserRole userRole;
    private UserStatus status;

    @Builder
    private User(String name, String nickname ,String email, String phone, Enrolled enrolled) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.enrolled = enrolled;
        this.userRole = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }
}
