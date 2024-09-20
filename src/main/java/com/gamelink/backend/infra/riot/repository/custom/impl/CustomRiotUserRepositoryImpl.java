package com.gamelink.backend.infra.riot.repository.custom.impl;

import com.gamelink.backend.domain.user.exception.NotSingleResultException;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.repository.custom.CustomRiotUserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomRiotUserRepositoryImpl implements CustomRiotUserRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<RiotUser> findOneByUserSubId(UUID userSubId) {
        List<RiotUser> riotUsers = entityManager.createQuery(
                "select ru from RiotUser ru where ru.user.subId = :subId",
                RiotUser.class
        )
                .setParameter("subId", userSubId)
                .getResultList();

        if (riotUsers.size() > 1) {
            throw new NotSingleResultException();
        } else if (riotUsers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(riotUsers.get(0));
        }
    }
}
