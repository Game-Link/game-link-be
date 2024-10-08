package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.MatchId;
import com.gamelink.backend.infra.riot.repository.custom.CustomMatchIdRepository;

public interface MatchIdRepository extends CustomJpaRepository<MatchId, Long>, CustomMatchIdRepository {
}
