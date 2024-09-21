package com.gamelink.backend.infra.riot.repository.custom.impl;

import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.repository.custom.CustomRankQueueRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomRankQueueRepositoryImpl implements CustomRankQueueRepository {

    private final EntityManager entityManager;

    @Override
    public List<RankQueue> findAllByRiotUserSubId(UUID riotUserSubId) {
        return entityManager.createQuery("select rq from RankQueue rq where rq.riotUser.subId = :subId", RankQueue.class)
                .setParameter("subId", riotUserSubId)
                .getResultList();
    }
}
