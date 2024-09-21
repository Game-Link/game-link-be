package com.gamelink.backend.infra.riot.repository.custom;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;

import java.util.List;
import java.util.UUID;

public interface CustomRankQueueRepository {
    List<RankQueue> findAllByRiotUserSubId(UUID riotUserSubId);
}
