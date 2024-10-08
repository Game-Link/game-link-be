package com.gamelink.backend.domain.user.model.entity;

import com.gamelink.backend.domain.user.model.Enrolled;
import com.gamelink.backend.domain.user.model.UserStatus;
import com.gamelink.backend.global.auth.UserRole;
import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private String uniqueId;

    @Enumerated(EnumType.STRING)
    private Enrolled enrolled;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ProfileImage> profileImages = new ArrayList<>();

    @Builder
    private User(String name, String nickname ,String email, String phone, String uniqueId, Enrolled enrolled) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.uniqueId = uniqueId;
        this.enrolled = enrolled;
        this.userRole = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }
}
