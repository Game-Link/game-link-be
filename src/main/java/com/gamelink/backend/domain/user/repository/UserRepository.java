package com.gamelink.backend.domain.user.repository;

import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.custom.CustomUserRepository;
import com.gamelink.backend.global.base.CustomJpaRepository;

import java.util.UUID;

public interface UserRepository extends CustomJpaRepository<User, Long>, CustomUserRepository {
}
