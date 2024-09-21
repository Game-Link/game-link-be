package com.gamelink.backend.infra.riot.repository;


import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.repository.custom.CustomRankQueueRepository;

public interface RankQueueRepository extends CustomJpaRepository<RankQueue, Long>, CustomRankQueueRepository {
}
