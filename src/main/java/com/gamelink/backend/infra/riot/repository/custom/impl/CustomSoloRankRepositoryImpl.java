package com.gamelink.backend.infra.riot.repository.custom.impl;

import com.gamelink.backend.domain.user.exception.NotSingleResultException;
import com.gamelink.backend.infra.riot.model.entity.queuetype.SoloRank;
import com.gamelink.backend.infra.riot.repository.custom.CustomSoloRankRepository;
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
public class CustomSoloRankRepositoryImpl implements CustomSoloRankRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<SoloRank> findOneByRiotUserSubId(UUID riotUserSubId) {
        List<SoloRank> result = entityManager.createQuery(
                        "select sr from SoloRank sr" +
                                " where sr.riotUser.subId = :riotUserSubId",
                        SoloRank.class
                )
                .setParameter("riotUserSubId", riotUserSubId)
                .getResultList();

        if (result.isEmpty()) {
            return Optional.empty();
        } else if (result.size() > 1) {
            throw new NotSingleResultException();
        } else {
            return Optional.of(result.get(0));
        }
    }
}
