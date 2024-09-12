package com.gamelink.backend.domain.user.repository.custom.impl;

import com.gamelink.backend.domain.user.exception.NotSingleResultException;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.custom.CustomUserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<User> findByName(String name) {
        List<User> users = entityManager.createQuery(
                "select u from User u where u.name = :name"
                , User.class)
                .setParameter("name", name)
                .getResultList();
        if (users.isEmpty()) {
            return Optional.empty();
        } else if (users.size() > 1) {
            throw new NotSingleResultException();
        } else {
            return Optional.ofNullable(users.get(0));
        }
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        List<User> users = entityManager.createQuery(
                "select u from User u where u.nickname = :nickname"
                , User.class)
                .setParameter("nickname", nickname)
                .getResultList();
        if (users.isEmpty()) {
            return Optional.empty();
        } else if (users.size() > 1) {
            throw new NotSingleResultException();
        } else {
            return Optional.ofNullable(users.get(0));
        }
    }
}
