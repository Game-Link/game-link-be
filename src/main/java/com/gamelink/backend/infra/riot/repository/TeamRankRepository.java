package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.queuetype.TeamRank;

public interface TeamRankRepository extends CustomJpaRepository<TeamRank, Long> {
}
