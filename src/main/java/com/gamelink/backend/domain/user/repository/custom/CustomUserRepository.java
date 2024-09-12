package com.gamelink.backend.domain.user.repository.custom;

import com.gamelink.backend.domain.user.model.entity.User;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> findByName(String name);

    Optional<User> findByNickname(String nickname);
}
