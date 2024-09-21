package com.gamelink.backend.infra.riot.repository.custom.impl;

import com.gamelink.backend.domain.user.exception.NotSingleResultException;
import com.gamelink.backend.infra.riot.model.entity.queuetype.TeamRank;
import com.gamelink.backend.infra.riot.repository.custom.CustomTeamRankRepository;
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
public class CustomTeamRankRepositoryImpl implements CustomTeamRankRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<TeamRank> findOneByRiotUserSubId(UUID subId) {
        List<TeamRank> result = entityManager.createQuery(
                        "select tr from TeamRank tr" +
                                " where tr.riotUser.subId = :subId",
                        TeamRank.class
                )
                .setParameter("subId", subId)
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
