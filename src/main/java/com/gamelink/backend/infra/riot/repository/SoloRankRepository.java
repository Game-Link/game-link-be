package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.queuetype.SoloRank;
import com.gamelink.backend.infra.riot.repository.custom.CustomSoloRankRepository;

public interface SoloRankRepository extends CustomJpaRepository<SoloRank, Long>, CustomSoloRankRepository {
}
