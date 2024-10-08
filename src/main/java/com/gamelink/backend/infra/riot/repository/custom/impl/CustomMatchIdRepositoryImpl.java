package com.gamelink.backend.infra.riot.repository.custom.impl;

import com.gamelink.backend.domain.user.exception.NotSingleResultException;
import com.gamelink.backend.infra.riot.model.entity.MatchId;
import com.gamelink.backend.infra.riot.repository.custom.CustomMatchIdRepository;
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
public class CustomMatchIdRepositoryImpl implements CustomMatchIdRepository {

    private final EntityManager entityManager;

    @Override
    public List<MatchId> findAllByRiotUserSubId(UUID subId) {
        return entityManager.createQuery("select m from MatchId m where m.riotUser.subId = :subId", MatchId.class)
                .setParameter("subId", subId)
                .getResultList();
    }

    @Override
    public int countByRiotUserSubId(UUID riotUserSubId) {
        return entityManager.createQuery("select count(m) from MatchId m where m.riotUser.subId = :subId", Long.class)
                .setParameter("subId", riotUserSubId)
                .getSingleResult()
                .intValue();
    }
}
